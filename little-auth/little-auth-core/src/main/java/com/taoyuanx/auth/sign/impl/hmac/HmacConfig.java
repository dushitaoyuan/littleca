package com.taoyuanx.auth.sign.impl.hmac;

import lombok.Data;

@Data
public class HmacConfig {
    private String hmacAlgorithm;
    private byte[] key;
}
