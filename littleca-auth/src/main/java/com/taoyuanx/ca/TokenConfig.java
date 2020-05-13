package com.taoyuanx.ca;

import com.auth0.jwt.algorithms.Algorithm;
import com.taoyuanx.ca.encode.api.Encode;

/**
 * @author 都市桃源
 * 2018年5月25日下午10:20:59
 */
public class TokenConfig {
    //token算法
    private Algorithm algorithm;
    //有效时间,默认30分钟
    private Long validTime;
    //私有属性加密器
    private Encode encode;
    private String defaultIss;
    private String defaultSub;

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Long getValidTime() {
        return validTime;
    }

    public void setValidTime(Long validTime) {
        this.validTime = validTime;
    }

    public Encode getEncode() {
        return encode;
    }

    public void setEncode(Encode encode) {
        this.encode = encode;
    }

    public String getDefaultIss() {
        return defaultIss;
    }

    public void setDefaultIss(String defaultIss) {
        this.defaultIss = defaultIss;
    }

    public String getDefaultSub() {
        return defaultSub;
    }

    public void setDefaultSub(String defaultSub) {
        this.defaultSub = defaultSub;
    }

    public TokenConfig() {
        super();
    }


}
