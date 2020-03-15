package com.taoyuanx.auth.sign;

import lombok.Data;

@Data
public class HmacConfig {
    private String hmacAlgorithm;
    private byte[] key;
}
