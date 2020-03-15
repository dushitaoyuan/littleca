package com.taoyuanx.ca.core.api.impl;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import com.taoyuanx.ca.core.api.AsymmetricalCipher;
import com.taoyuanx.ca.core.api.AsymmetricalSignature;
import com.taoyuanx.ca.core.api.AsymmetricalUtil;
import com.taoyuanx.ca.core.util.Util;

/**
 * rsa非对称操作 实现
 * @author 都市桃源
 * 2018年7月1日下午10:36:40
 */
public class RSA implements AsymmetricalCipher,AsymmetricalSignature{
	
	@Override
	public byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
		Cipher cipher = AsymmetricalUtil.getCipherInstance(AsymmetricalCipher.RSA_NOPADDING_CIPHER);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11,如果明文长度大于模长-11则要分组加密
		key_len -= 11;
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		for (int i = 0; i < data.length; i += key_len) {
			byte[] doFinal = cipher.doFinal(Util.subarray(data, i, i +key_len));
			out.write(doFinal);
		}
		return out.toByteArray();
	}

	@Override
	public byte[] decrypt(byte[] data, PrivateKey privateKey) throws Exception {
		
		Cipher cipher = AsymmetricalUtil.getCipherInstance(AsymmetricalCipher.RSA_NOPADDING_CIPHER);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 模长
		int key_len = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
		// 分组解密
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		// 如果密文长度大于模长则要分组解密
		for (int i = 0; i <data.length ; i += key_len) {
			byte[] doFinal = cipher.doFinal(Util.subarray(data, i, i + key_len));
			out.write(doFinal);
		}
		return out.toByteArray();
	}

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



}
