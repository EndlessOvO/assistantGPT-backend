package com.endlessovo.assistantGPT.common.filter;

import com.alibaba.fastjson2.JSON;
import com.endlessovo.assistantGPT.common.ResponseVO;
import com.endlessovo.assistantGPT.common.config.JWTConfig;
import com.endlessovo.assistantGPT.common.constant.CacheConstant;
import com.endlessovo.assistantGPT.common.exception.CustomException;
import com.endlessovo.assistantGPT.common.exception.CustomExceptionEnum;
import com.endlessovo.assistantGPT.common.util.RedisUtil;
import com.endlessovo.assistantGPT.common.util.RequestContextUtil;
import com.endlessovo.assistantGPT.common.util.VirtualThreadUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * JWT 认证过滤器
 * 用来对请求进行权限认证
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTConfig config;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain chain) {
        // 针对白名单接口放行
        if(config.getWhiteList().getEnabled() && existWhiteList(request)) {
            try {
                chain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                response(ResponseVO.fail(CustomExceptionEnum.SERVICE_ERROR), response);
            }
            return;
        }

        // 其余接口进行token校验
        String token;
        try {
            token = RequestContextUtil.getAuthorization();
        } catch (CustomException exception) {
            response(ResponseVO.fail(CustomExceptionEnum.LOGIN_EXPIRE), response);
            return;
        }
        if (!RedisUtil.exist(CacheConstant.TOKEN_PREFIX + token)) {
            response(ResponseVO.fail(CustomExceptionEnum.LOGIN_EXPIRE), response);
            return;
        }
        VirtualThreadUtil.run(() -> refreshToken(token));

        // 校验通过，放行
        try {
            chain.doFilter(request, response);
        } catch (ServletException | IOException exception) {
            response(ResponseVO.fail(CustomExceptionEnum.LOGIN_EXPIRE), response);
        }
    }

    /**
     * 检验否存在于白名单
     * @param request HTTP请求
     * @return 是否存在于白名单
     */
    private boolean existWhiteList(HttpServletRequest request) {
        // 支持正则匹配
        return config.getWhiteList()
                .getPaths()
                .stream()
                .anyMatch(path -> request.getRequestURI().matches(path));
    }

    /**
     * 响应 - 只用来响应Token失效或错误的情况
     *
     * @param result   响应体
     * @param response HTTP响应
     */
    @SneakyThrows
    private void response(ResponseVO<Void> result, HttpServletResponse response) {
        // 1. 构建响应体并添加请求头
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(result));
        writer.flush();
        writer.close();
    }

    private void refreshToken(String token) {
        Long expire = RedisUtil.getExpireTime(CacheConstant.TOKEN_PREFIX + token, TimeUnit.HOURS);
        if (expire > CacheConstant.TOKEN_REFRESH_LIMIT_HOURS) return;
        RedisUtil.extendExpireTime(CacheConstant.TOKEN_PREFIX + token, CacheConstant.TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
    }
}