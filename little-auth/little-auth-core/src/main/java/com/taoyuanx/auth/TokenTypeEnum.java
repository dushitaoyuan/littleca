package com.taoyuanx.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * token 类型枚举
 */
public enum TokenTypeEnum {
    BUSSINESS(1, "业务token"),
    REFRESH(2, "refresh token");
    public int code;
    public String desc;
    private static final Map<Integer, TokenTypeEnum> enumHolder = new HashMap<>();

    static {
        TokenTypeEnum[] typeArray = TokenTypeEnum.values();
        Arrays.stream(typeArray).forEach(typeEnum -> {
            enumHolder.put(typeEnum.code, typeEnum);
        });
    }

    TokenTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TokenTypeEnum type(Integer type) {
        return enumHolder.get(type);

    }
}