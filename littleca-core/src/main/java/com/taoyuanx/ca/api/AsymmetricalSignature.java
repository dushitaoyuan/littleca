package com.taoyuanx.ca.api;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 非对称签名,验签
 * @author 都市桃源
 * 2018年7月2日下午10:27:38
 * TODO
 */
public interface AsymmetricalSignature {
	/**
	 * 签名验签
	 * @return
	 */
	byte[]  sign(byte[] data,PrivateKey privateKey,String signAlgorithm) throws Exception;
	boolean verifySign(byte[] signData,byte[] content,PublicKey publicKey,String signAlgorithm) throws Exception;
}
