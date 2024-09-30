package com.endlessovo.assistantGPT.common.constant;

public record CacheConstant() {
    public static String PROJECT_PREFIX = "assistantGPT:";
    public static String TOKEN_PREFIX = PROJECT_PREFIX + "token:";
    public static String LOGIN_CREDENTIALS_PREFIX = PROJECT_PREFIX + "login_credentials:";
    /**
     * token 过期时间 48 小时
     */
    public static Integer TOKEN_EXPIRE_HOURS = 48;

    /**
     * token 刷新时间 2 小时 - 两小时内过期则触发刷新
     */
    public static Integer TOKEN_REFRESH_LIMIT_HOURS = 2;
}
