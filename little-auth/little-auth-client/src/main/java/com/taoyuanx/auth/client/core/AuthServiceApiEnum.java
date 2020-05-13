package com.taoyuanx.auth.client.core;

/**
 * api 概览
 */
public enum AuthServiceApiEnum {
    AUTH("v1/auth", "token获取"),
    AUTH_REFRESH("v1/auth/refresh", "token续期"),
    TIMESTAMP("timestamp", "时间戳服务"),
    HELLO("hello", "心跳监测"),
    ;
    public String path;
    public String desc;

    AuthServiceApiEnum(String path, String desc) {
        this.path = path;
        this.desc = desc;
    }
}