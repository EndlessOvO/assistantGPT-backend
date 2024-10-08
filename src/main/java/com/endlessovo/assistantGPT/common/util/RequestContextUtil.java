package com.endlessovo.assistantGPT.common.util;

import com.endlessovo.assistantGPT.common.constant.CacheConstant;
import com.endlessovo.assistantGPT.common.exception.CustomException;
import com.endlessovo.assistantGPT.common.exception.CustomExceptionEnum;
import com.endlessovo.assistantGPT.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * 请求上下文工具类
 */
@UtilityClass
public class RequestContextUtil {
    private static final String AUTHORIZATION = "Authorization";

    public Object getAttributeByRequestContextHolder(String attributeName) {
        return getServletRequest().getAttribute(attributeName);
    }

    public <T> void setAttributeByRequestContextHolder(String attributeName, T attributeValue) {
        getServletRequest().setAttribute(attributeName, attributeValue);
    }

    /**
     * 获得当前请求的 URI
     * @return String URL
     */
    public static String getRequestURI() {
        return getServletRequest().getRequestURI();
    }

    /**
     * 获得当前请求的 IP
     * @return String IP
     */
    public static String getRequestIP(){
        return getServletRequest().getRemoteAddr();
    }

    private HttpServletRequest getServletRequest() {
        return ((ServletRequestAttributes)
                Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                        .orElseThrow(() -> new CustomException(CustomExceptionEnum.CONTEXT_CATCH_ERROR)))
                .getRequest();
    }

    public static String getAuthorization() {
        HttpServletRequest request = getServletRequest();
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .orElseThrow(() -> new CustomException(CustomExceptionEnum.AUTHORIZATION_NOT_FOUND));
    }

    /**
     * 获取当前用户
     * @return User
     */
    public static User getCurrentUser() {
        String authorization;
        try {
            authorization = getAuthorization();
        } catch (CustomException e) {
//            不存在请求头就认为是匿名用户
            return null;
        }
        return RedisUtil.get(CacheConstant.TOKEN_PREFIX + authorization, User.class);
    }
}
