package com.endlessovo.assistantGPT.common;

import com.endlessovo.assistantGPT.common.exception.CustomExceptionEnum;
import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
public class ResponseVO<T> {
    private Integer code;
    private T data;
    private String message;

    private final static Integer HTTP_SUCCESS_CODE = 200;
    private final static String HTTP_SUCCESS_MESSAGE = "success";

    private ResponseVO() {}

    public static <T> ResponseVO<T> success(T data) {
        return ResponseVO.<T>builder()
                .code(HTTP_SUCCESS_CODE)
                .data(data)
                .message(HTTP_SUCCESS_MESSAGE)
                .build();
    }

    /**
     * 服务器异常失败
     * @return ResponseVO
     */
    public static ResponseVO<Void> fail(CustomExceptionEnum exception) {
        return ResponseVO.<Void>builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

    /**
     * 服务器异常失败
     * @return ResponseVO
     */
    public static ResponseVO<Void> fail(Integer code, String message) {
        return ResponseVO.<Void>builder()
                .code(code)
                .message(message)
                .build();
    }
}
