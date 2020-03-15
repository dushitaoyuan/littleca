package com.taoyuanx.auth.mac;


public enum HMacAlgorithms {
    /**
     * md系列
     */
    HMAC_MD2("HmacMD2"),
    HMAC_MD4("HmacMD4"),
    HMAC_MD5("HmacMD5"),
    /**
     * sha系列
     */
    HMAC_SHA_1("HmacSHA1"),
    HMAC_SHA_224("HmacSHA224"),
    HMAC_SHA_256("HmacSHA256"),
    HMAC_SHA_384("HmacSHA384"),
    HMAC_SHA_512("HmacSHA512"),


    HMAC_SHA_512_224("HMACSHA512/224"),
    HMAC_SHA_512_256("HMACSHA512/256"),
    HMAC_SHA_3_224("HmacSHA3-224"),
    HMAC_SHA_3_256("HmacSHA3-256"),
    HMAC_SHA_3_384("HmacSHA3-384"),
    HMAC_SHA_3_512("HmacSHA3-512"),


    /**
     * ripemd 系列
     */
    HMAC_RIPEMD128("HmacRIPEMD128"),
    HMAC_RIPEMD160("HmacRIPEMD160"),
    HMAC_RIPEMD256("HmacRIPEMD256"),
    HMAC_RIPEMD320("HmacRIPEMD320"),

    /**
     * skein 系列
     */
    HMAC_Skein_256_128("HMACSkein-256-128"),
    HMAC_Skein_256_160("HMACSkein-256-160"),
    HMAC_Skein_256_224("HMACSkein-256-224"),
    HMAC_Skein_256_256("HMACSkein-256-256"),
    HMAC_Skein_512_128("HMACSkein-512-128"),
    HMAC_Skein_512_160("HMACSkein-512-160"),
    HMAC_Skein_512_224("HMACSkein-512-224"),
    HMAC_Skein_512_256("HMACSkein-512-256"),
    HMAC_Skein_512_384("HMACSkein-512-384"),
    HMAC_Skein_512_512("HMACSkein-512-512"),
    HMAC_Skein_1024_384("HMACSkein-1024-384"),
    HMAC_Skein_1024_512("HMACSkein-1024-512"),


    /**
     * keccak 系列
     */
    HMAC_KECCAK_224("HMACKECCAK224"),
    HMAC_KECCAK_256("HMACKECCAK256"),
    HMAC_KECCAK_288("HMACKECCAK288"),
    HMAC_KECCAK_384("HMACKECCAK384"),
    HMAC_KECCAK_512("HMACKECCAK512"),
    /**
     * DSTU7564 系列
     */
    HMAC_DSTU7564_256("HMACDSTU7564-256"),
    HMAC_DSTU7564_384("HMACDSTU7564-384"),
    HMAC_DSTU7564_512("HMACDSTU7564-512"),


    /**
     * 其他
     */
    HMAC_GOST3411("HmacGOST3411"),
    HMAC_GOST3411_2012_256("HmacGOST3411-2012-256"),
    HMAC_GOST3411_2012_512("HmacGOST3411-2012-512"),
    HMAC_TIGER("HmacTiger"),
    HMAC_WHIRLPOOL("HMACWHIRLPOOL"),
    /**
     * 国密系列
     */
    HMAC_SM3("SM3");

    private final String name;

    private HMacAlgorithms(final String algorithm) {
        this.name = algorithm;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
