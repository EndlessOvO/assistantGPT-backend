package com.endlessovo.assistantGPT.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 */
@Getter
@NoArgsConstructor
public class CustomException extends RuntimeException {
    private Integer code;
    private String message;

    /**
     * 未定义异常 - 直接抛出的情况
     * @param message 异常信息
     */
    public CustomException(String message) {
        super(message);
        this.code = CustomExceptionEnum.SERVICE_ERROR.getCode();
        this.message = CustomExceptionEnum.SERVICE_ERROR.getMessage();
    }

    /**
     * 已定义异常 - 通过枚举类抛出的情况
     * @param exception 异常枚举
     */
    public CustomException(CustomExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.message = exception.getMessage();
    }
}
