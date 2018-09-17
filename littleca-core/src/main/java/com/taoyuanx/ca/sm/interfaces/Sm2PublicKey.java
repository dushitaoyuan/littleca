package com.taoyuanx.ca.sm.interfaces;

import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.interfaces.ECPublicKey;

/**
 * @author 都市桃源
 * 2018年9月13日 上午11:15:35
 * userfor: sm2 私钥
 */
public interface Sm2PublicKey extends ECPublicKey {
	ECPublicKeyParameters getPublicKeyParameters();
	 byte[] getWithId();
	 void setWithId(byte[] withId);
}
