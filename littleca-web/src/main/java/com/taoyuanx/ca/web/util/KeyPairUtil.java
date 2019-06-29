package com.taoyuanx.ca.web.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import com.taoyuanx.ca.sm.interfaces.Sm2KeyPair;
import com.taoyuanx.ca.sm.interfaces.impl.Sm2KeyPairImpl;
import com.taoyuanx.ca.web.common.CAConstant.KeyType;
import com.taoyuanx.ca.web.dto.KeyPairResult;

/**
 * @author 都市桃源
 * 2018年9月14日 下午3:52:35
 * 用于 生成所有支持的秘钥对类型
*/
public class KeyPairUtil {
	public static final KeyPairResult gen(KeyType type, Integer keySize) throws Exception {
		KeyPairResult keyPairResult = new KeyPairResult();
		switch (type) {
		case RSA:
		case DSA:{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(type.name, BouncyCastleProvider.PROVIDER_NAME);
			if(null!=keySize) {
				kpg.initialize(keySize);
			}
			KeyPair keyPair = kpg.generateKeyPair();
			keyPairResult.setPri(keyPair.getPrivate());
			keyPairResult.setPub(keyPair.getPublic());
			return keyPairResult;
		}
		case ECDSA:{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(type.name, BouncyCastleProvider.PROVIDER_NAME);
			KeyPair keyPair = kpg.generateKeyPair();
			keyPairResult.setPri(keyPair.getPrivate());
			keyPairResult.setPub(keyPair.getPublic());
			return keyPairResult;
		}
		case SM2: {
			Sm2KeyPair sm2KeyPair = new Sm2KeyPairImpl();
			keyPairResult.setPri(sm2KeyPair.getPrivate());
			keyPairResult.setPub(sm2KeyPair.getPublic());
			return keyPairResult;
		}
		}
		return null;
	}

	public final static String convertPublicKeyToPemString(PublicKey pub) {
		try {
			if (null == pub) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			OutputStreamWriter outWriter = new OutputStreamWriter(out);
			JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(outWriter);
			jcaPEMWriter.writeObject(pub);
			jcaPEMWriter.close();
			return new String(out.toByteArray());
		} catch (Exception e) {
			return null;
		}
	}

	public final static String convertPrivateKeyToPemString(PrivateKey pri) {
		try {
			if (null == pri) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			OutputStreamWriter outWriter = new OutputStreamWriter(out);
			JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(outWriter);
			jcaPEMWriter.writeObject(pri);
			jcaPEMWriter.close();
			return new String(out.toByteArray());
		} catch (Exception e) {
			return null;
		}
	}

}
