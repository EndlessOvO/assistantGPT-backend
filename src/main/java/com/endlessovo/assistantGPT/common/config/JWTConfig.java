package com.endlessovo.assistantGPT.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {
    private String issuer;
    private String secret;
    private WhiteList whiteList;

    /**
     * 白名单列表
     */
    @Getter
    @Setter
    public static class WhiteList {
        private Boolean enabled;
        private ArrayList<String> paths;
    }
}
