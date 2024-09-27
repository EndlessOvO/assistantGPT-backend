package com.endlessovo.assistantGPT.common.handler;

import com.alibaba.fastjson2.JSON;
import com.endlessovo.assistantGPT.common.util.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 全局接口请求日志处理器
 */
@Slf4j
@Aspect
@Component
public class GlobalRequestLogHandler {

    @Pointcut("execution(public * com.endlessovo.assistantGPT.controller..*.*(..))")
    public void controller(){}

    @Around("controller()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String url = RequestContextUtil.getRequestURI();
        String ip = RequestContextUtil.getRequestIP();
        // 请求参数
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 类路径URL
        String methodPackage = methodSignature.getDeclaringTypeName();
        // 方法名
        String method = methodSignature.getMethod().getName();
        log.info("[请求开始]");
        log.info("[请求IP]：{}", ip);
        log.info("[请求URL]：{}", url);
        log.info("[请求方法]：{}", methodPackage + "#" + method);
        log.info("[请求参数]：{}", JSON.toJSONString(args));
        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            long time = System.currentTimeMillis() - start;
            log.info("[请求异常]");
            log.info("[请求耗时]：{}", time + "ms");
            log.info("[请求结束]");
            throw e;
        }
        long time = System.currentTimeMillis() - start;
        log.info("[请求耗时]：{}", time + "ms");
        log.info("[请求结束]");
        return result;
    }
}
