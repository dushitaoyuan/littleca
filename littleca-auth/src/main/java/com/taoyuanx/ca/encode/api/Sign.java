package com.taoyuanx.ca.encode.api;

/**
 * @author 都市桃源
 * 签名接口
 */
public interface Sign {
	/**
	 * 签名
	 * @param data
	 * @return
	 */
	byte[] sign(byte[] data);
	/**
	 * 验签
	 * @param data
	 * @param signData
	 * @return
	 */
	boolean verify(byte[] data, byte[] signData);
}
