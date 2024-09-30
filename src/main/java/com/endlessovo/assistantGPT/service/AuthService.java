package com.endlessovo.assistantGPT.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import com.endlessovo.assistantGPT.common.constant.CacheConstant;
import com.endlessovo.assistantGPT.common.exception.CustomException;
import com.endlessovo.assistantGPT.common.exception.CustomExceptionEnum;
import com.endlessovo.assistantGPT.common.util.*;
import com.endlessovo.assistantGPT.model.entity.User;
import com.endlessovo.assistantGPT.model.vo.user.UserLoginQuery;
import com.endlessovo.assistantGPT.model.vo.user.UserLoginVO;
import com.endlessovo.assistantGPT.model.vo.user.UserRegisterQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    private static final Integer LOGIN_CODE_EXPIRE_TIME_MINUTES = 5;

    public UserLoginVO login(UserLoginQuery userLoginQuery) {
        checkRepeatLogin();
//        1. 根据 email 查询用户
        User user = userService.lambdaQuery()
                .eq(User::getEmail, userLoginQuery.getEmail())
                .oneOpt()
                .orElseThrow(() -> new CustomException(CustomExceptionEnum.USER_NOT_EXIST));
//        2. 校验密码
        String encodedPassword = MD5.create().digestHex(userLoginQuery.getPassword());
        if (!encodedPassword.equals(user.getPassword())) throw new CustomException(CustomExceptionEnum.USER_LOGIN_ERROR);
//        3. 生成 token
        String token = JWTUtil.generateToken(user.getEmail());
//        4. 缓存 用户信息
        VirtualThreadUtil.run(() -> RedisUtil.set(CacheConstant.TOKEN_PREFIX + token, user, CacheConstant.TOKEN_EXPIRE_HOURS, TimeUnit.MINUTES));
        return UserLoginVO.builder()
                .id(user.getId())
                .name(user.getName())
                .token(token)
                .build();
    }

    public UserLoginVO login(String credentials) {
        checkRepeatLogin();
//        1. 从 redis 中获取 登录凭证
        User user;
        try {
            user = RedisUtil.get(CacheConstant.LOGIN_CREDENTIALS_PREFIX + credentials, User.class);
        } catch (CustomException e) {
            throw new CustomException(CustomExceptionEnum.USER_NOT_EXIST);
        }
//        2. 生成 token
        String token = JWTUtil.generateToken(user.getEmail());
//        3. 缓存 用户信息
        VirtualThreadUtil.run(() -> RedisUtil.set(CacheConstant.TOKEN_PREFIX + token, user, CacheConstant.TOKEN_EXPIRE_HOURS, TimeUnit.MINUTES));
        return UserLoginVO.builder()
                .id(user.getId())
                .name(user.getName())
                .token(token)
                .build();
    }

    public void sendLoginEmail(String email) {
        checkRepeatLogin();
//        1. 检查 email 是否存在
        User user = userService.lambdaQuery()
                .eq(User::getEmail, email)
                .oneOpt()
                .orElseThrow(() -> new CustomException(CustomExceptionEnum.USER_NOT_EXIST));
//        2. 生成 登录凭证 存入redis并发邮件
        String credentials = IdUtil.fastSimpleUUID();
        VirtualThreadUtil.run(() -> {
            RedisUtil.set(CacheConstant.LOGIN_CREDENTIALS_PREFIX + credentials, user, LOGIN_CODE_EXPIRE_TIME_MINUTES, TimeUnit.MINUTES);
            EmailUtil.sendLoginEmail(email, credentials);
        });
    }

    public void register(UserRegisterQuery userRegisterQuery) {
//        1. 检查 email 是否存在
        Optional<User> userOpt = userService.lambdaQuery()
                .eq(User::getEmail, userRegisterQuery.getEmail())
                .oneOpt();
        if (userOpt.isPresent()) throw new CustomException(CustomExceptionEnum.USER_EXIST);
        VirtualThreadUtil.run(() -> createUser(userRegisterQuery));
    }

    private void createUser(UserRegisterQuery userRegisterQuery) {
        User user = User.builder()
                .email(userRegisterQuery.getEmail())
                .name(userRegisterQuery.getName())
                .password(MD5.create().digestHex(userRegisterQuery.getPassword()))
                .build();
        userService.save(user);
    }

    /**
     * 避免重复登录
     */
    public void checkRepeatLogin() {
        try {
            RequestContextUtil.getCurrentUser();
        } catch (CustomException e) {
//            用户未登录 - 直接放行
            return;
        }
        throw new CustomException(CustomExceptionEnum.USER_REPEAT_LOGIN);
    }
}
