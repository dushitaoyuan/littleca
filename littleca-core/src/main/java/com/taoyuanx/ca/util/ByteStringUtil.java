package com.taoyuanx.ca.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;

/**
 * 字符和byte数组 转换工具
 * @author 都市桃源
 * 2018年7月1日下午10:13:47
 * TODO
 */
public class ByteStringUtil {
	/*
	 * 编码类型
	 */
	public static final int BASE64=1;
	public static final int HEX=2;
	public static final int ASCII=3;
	public static String  toString(byte[] data,int type) throws Exception {
		switch (type) {
		case BASE64:
			return Base64.encodeBase64String(data);
		case HEX:
			return Hex.encodeHexString(data);
		case ASCII:
			return BinaryCodec.toAsciiString(data);
		default:
			return Base64.encodeBase64String(data);
		}
	} 
	
	public static byte[] toBytes(String data,int type) throws Exception {
			switch (type) {
			case BASE64:
				return Base64.decodeBase64(data);
			case HEX:
				return Hex.decodeHex(data);
			case ASCII:
				return BinaryCodec.fromAscii(data.getBytes());
			default:
				return Base64.decodeBase64(data);
			}
	}
	
	
	
}
