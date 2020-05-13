package com.taoyuanx.ca.util;

import com.taoyuanx.ca.encode.eum.StringByteType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;

/**
 * @author 都市桃源
 * 2018年5月29日下午10:31:53
 * 字节数组和字符串转换工具类
 */
public class BytesStringUtil {


    public static final String toString(byte[] data, StringByteType type) throws Exception {
        switch (type) {
            case HEX:
                return Hex.encodeHexString(data);
            case BASE64:
                return Base64.encodeBase64String(data);
            case BA64_URL_SAFE:
                return Base64.encodeBase64URLSafeString(data);
            case ASSCII:
                return BinaryCodec.toAsciiString(data);
        }
        return null;
    }

    public static final byte[] toBytes(String data, StringByteType type) throws Exception {
        switch (type) {
            case HEX:
                return Hex.decodeHex(data.toCharArray());
            case BASE64:
            case BA64_URL_SAFE:
                return Base64.decodeBase64(data);
            case ASSCII:
                return BinaryCodec.fromAscii(data.getBytes());

        }
        return null;
    }

}
