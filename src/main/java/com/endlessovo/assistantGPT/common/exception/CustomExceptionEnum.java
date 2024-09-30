package com.endlessovo.assistantGPT.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomExceptionEnum {
    SERVICE_ERROR(500, "服务器异常"),
    PARAM_ERROR(400, "请求无效"),
    USER_UNLOGIN_ERROR(401, "用户未登录"),
    USER_LOGIN_ERROR(402, "用户登录失败"),
    NO_PERMISSION_ERROR(403, "无权限访问"),
    CONTEXT_CATCH_ERROR(1001, "无法获取请求上下文"),
    DATASOURCE_ACCESS_ERROR(1002, "数据源访问异常"),
    USER_NOT_EXIST(1003, "用户不存在"),
    USER_EXIST(1004, "用户已经存在"),
    REDIS_NOT_EXIST(1005, "Redis中不存在值"),
    AUTHORIZATION_NOT_FOUND(1006, "请求头中未找到 Authorization"),
    LOGIN_EXPIRE(1007, "登录已过期，请重新登录"),
    USER_REPEAT_LOGIN(1008, "请勿重复登录"),
    ;
    private final Integer code;
    private final String message;
}
