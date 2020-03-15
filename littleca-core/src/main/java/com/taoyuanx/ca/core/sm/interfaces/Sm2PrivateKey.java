package com.taoyuanx.ca.core.sm.interfaces;

import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jce.interfaces.ECPrivateKey;

/**
 * @author 都市桃源
 * 2018年9月13日 上午11:15:35
 * userfor: sm2 私钥
 */
public interface Sm2PrivateKey  extends ECPrivateKey{
	 ECPrivateKeyParameters getPrivateKeyParameters();
	 byte[] getWithId();
	 void setWithId(byte[] withId);
}
