package com.endlessovo.assistantGPT.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.endlessovo.assistantGPT.common.config.JWTConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 工具类
 */
@Slf4j
@Component
public class JWTUtil {
    private static JWTConfig jwtConfig;

    @Autowired
    public JWTUtil(JWTConfig config) {
        jwtConfig = config;
        log.info("[JWTUtil]初始化完成");
    }

    /**
     * 生成Token
     * @param subject 用户账户名
     * @return Token
     */
    public static String generateToken(String subject) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        return JWT.create()
                .withIssuer(jwtConfig.getIssuer())
                .withIssuedAt(new Date())
                .withSubject(subject)
                .sign(algorithm);
    }

    /**
     * 验证Token合法性
     * @param token 传入唯一Token
     * @return see@DecodedJWT, 可以获取Token中的信息
     */
    public static DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        return JWT.require(algorithm)
                .withIssuer(jwtConfig.getIssuer())
                .build()
                .verify(token);
    }
}
