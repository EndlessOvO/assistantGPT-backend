package com.endlessovo.assistantGPT.common.util;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis 工具类
 */
@Slf4j
@Component
@SuppressWarnings("unchecked")
public class RedisUtil {
    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> rt) {
        redisTemplate = rt;
        log.info("[RedisUtil]初始化完成");
    }

    public static boolean exist(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public static Set<String> getAllKey() {
        return redisTemplate.keys("*");
    }

    public static Set<String> getKeysByPrefix(String prefix) {
        return redisTemplate.keys(prefix + "*");
    }

    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static <T> void set(String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public static Long getExpireTime(String key, TimeUnit timeUnit) {
        return Optional.ofNullable(redisTemplate.getExpire(key, timeUnit)).orElse(0L);
    }

    public static void extendExpireTime(String key, long time, TimeUnit timeUnit) {
        set(key, get(key), time, timeUnit);
    }
}