package com.endlessovo.assistantGPT.common.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置
 */
@Slf4j
@Configuration
@SuppressWarnings({"squid:S2440"})
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 使用FastJson2进行序列化，支持对象的序列化和反序列化
        Fastjson2JsonRedisSerializer<Object> fastjson2JsonRedisSerializer = new Fastjson2JsonRedisSerializer<>(Object.class);
        // 设置键和值的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(fastjson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(fastjson2JsonRedisSerializer);

        // 启用事务支持
        redisTemplate.setEnableTransactionSupport(true);

        // 初始化RedisTemplate
        redisTemplate.afterPropertiesSet();

        log.info("[Redis自定义配置] 初始化完成");
        return redisTemplate;
    }

    /**
     * FastJson2序列化器
     *
     * @param <T> 泛型
     */
    private record Fastjson2JsonRedisSerializer<T>(Class<T> clazz) implements RedisSerializer<T> {

        @Override
        public byte[] serialize(T t) throws SerializationException {
            return JSON.toJSONBytes(t, JSONWriter.Feature.WriteClassName);
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length == 0) return null;
            return JSON.parseObject(bytes, clazz);
        }
    }
}
