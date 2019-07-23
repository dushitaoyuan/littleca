package com.taoyuanx.ca.tsa;

/**
 * @author dushitaoyuan
 * @desc tsa常量
 * @date 2019/7/10
 */
public interface TsaTranserConstant {
    public static final String TIMESTAMP_QUERY_CONTENT_TYPE = "application/timestamp-query";
    public static final String TIMESTAMP_REPLY_CONTENT_TYPE = "application/timestamp-reply";
    public static final String TRANSFER_ENCODING_HEADER = "Content-Transfer-Encoding";
    public static final String TRANSFER_ENCODING_BASE64 = "BASE64";
    public static final String TRANSFER_ENCODING_BINARY = "BINARY";


     static boolean isBase64(String encoding){
        if (encoding == null) {
            return false;
        }
        if (TRANSFER_ENCODING_BASE64.equalsIgnoreCase(encoding)) {
            return true;
        }
        return false;
    }
}
