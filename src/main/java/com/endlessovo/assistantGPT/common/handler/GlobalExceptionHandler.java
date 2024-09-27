package com.endlessovo.assistantGPT.common.handler;

import com.endlessovo.assistantGPT.common.ResponseVO;
import com.endlessovo.assistantGPT.common.exception.CustomException;
import com.endlessovo.assistantGPT.common.exception.CustomExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseVO<Void> handleCustomExceptions(CustomException exception) {
        log.error("**************CustomException**************");
        printExceptionInfo(exception.getMessage() + exception.getCause());
        return ResponseVO.fail(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseVO<Void> handleRuntimeExceptions(RuntimeException exception) {
        log.error("**************RuntimeException**************");
        printExceptionInfo(exception.getMessage() + exception.getCause());
        return ResponseVO.fail(CustomExceptionEnum.SERVICE_ERROR.getCode(), exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseVO<Void> handleOtherExceptions(Exception exception) {
        log.error("**************Exception**************");
        printExceptionInfo(exception.getMessage() + exception.getCause());
        return ResponseVO.fail(CustomExceptionEnum.SERVICE_ERROR.getCode(), exception.getMessage());
    }

    private void printExceptionInfo(String message) {
        log.error("【异常信息】：{}", message);
    }
}
