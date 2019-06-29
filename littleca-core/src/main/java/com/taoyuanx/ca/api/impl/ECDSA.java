package com.taoyuanx.ca.api.impl;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import com.taoyuanx.ca.api.AsymmetricalCipher;
import com.taoyuanx.ca.api.AsymmetricalSignature;
import com.taoyuanx.ca.api.AsymmetricalUtil;

/**
 * rsa非对称操作 实现
 * @author 都市桃源
 * 2018年7月1日下午10:36:40
 */
public class ECDSA implements AsymmetricalSignature,AsymmetricalCipher{
	


	@Override
	public byte[] sign(byte[] data, PrivateKey privateKey, String signAlgorithm) throws Exception {
		Signature signature = AsymmetricalUtil.getSignatureInstance(signAlgorithm);
		signature.initSign(privateKey);
		signature.update(data);
		return signature.sign();
	}

	@Override
	public boolean verifySign(byte[] signData, byte[] content, PublicKey publicKey, String signAlgorithm) throws Exception {
		Signature signature = AsymmetricalUtil.getSignatureInstance(signAlgorithm);
		signature.initVerify(publicKey);
		signature.update(content);
		return signature.verify(signData);
	}

	@Override
	public byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
		
		Cipher cipher = AsymmetricalUtil.getCipherInstance(AsymmetricalCipher.ECIES_CIPHER);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] doFinal = cipher.doFinal(data);
		return doFinal;
	}

	@Override
	public byte[] decrypt(byte[] data, PrivateKey privateKey) throws Exception {

		Cipher cipher = AsymmetricalUtil.getCipherInstance(AsymmetricalCipher.ECIES_CIPHER);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] doFinal = cipher.doFinal(data);
		return doFinal;
	}



}
