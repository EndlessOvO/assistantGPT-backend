package com.endlessovo.assistantGPT.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import com.endlessovo.assistantGPT.common.exception.CustomException;
import com.endlessovo.assistantGPT.common.exception.CustomExceptionEnum;
import com.endlessovo.assistantGPT.common.util.EmailUtil;
import com.endlessovo.assistantGPT.common.util.JWTUtil;
import com.endlessovo.assistantGPT.common.util.RedisUtil;
import com.endlessovo.assistantGPT.common.util.VirtualThreadUtil;
import com.endlessovo.assistantGPT.model.entity.User;
import com.endlessovo.assistantGPT.model.vo.user.UserLoginQuery;
import com.endlessovo.assistantGPT.model.vo.user.UserLoginVO;
import com.endlessovo.assistantGPT.model.vo.user.UserRegisterQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserService userService;

    private static final Integer LOGIN_CODE_EXPIRE_TIME_MINUTES = 60;

    public UserLoginVO login(UserLoginQuery userLoginQuery) {
//        1. 根据 email 查询用户
        User user = userService.lambdaQuery()
                .eq(User::getEmail, userLoginQuery.getEmail())
                .oneOpt()
                .orElseThrow(() -> new CustomException(CustomExceptionEnum.USER_NOT_EXIST));
//        2. 校验密码
        String encodedPassword = MD5.create().digestHex(userLoginQuery.getPassword());
        if (!encodedPassword.equals(userLoginQuery.getPassword())) throw new CustomException(CustomExceptionEnum.USER_LOGIN_ERROR);
//        3. 生成 token
        String token = JWTUtil.generateToken(user.getEmail());
        return UserLoginVO.builder()
                .id(user.getId())
                .name(user.getName())
                .token(token)
                .build();
    }

    public UserLoginVO login(String credentials) {
//        1. 从 redis 中获取 登录凭证
        User user = RedisUtil.get(credentials, User.class);
//        2. 生成 token
        String token = JWTUtil.generateToken(user.getEmail());
        return UserLoginVO.builder()
                .id(user.getId())
                .name(user.getName())
                .token(token)
                .build();
    }

    public void sendLoginEmail(String email) {
//        1. 检查 email 是否存在
        User user = userService.lambdaQuery()
                .eq(User::getEmail, email)
                .oneOpt()
                .orElseThrow(() -> new CustomException(CustomExceptionEnum.USER_NOT_EXIST));
//        2. 生成 登录凭证 存入redis
        String credentials = IdUtil.fastSimpleUUID();
        VirtualThreadUtil.run(() -> RedisUtil.set(credentials, user, LOGIN_CODE_EXPIRE_TIME_MINUTES, TimeUnit.MINUTES));
//        2. 发送邮件
        VirtualThreadUtil.run(() -> EmailUtil.sendLoginEmail(email, credentials));
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
}
