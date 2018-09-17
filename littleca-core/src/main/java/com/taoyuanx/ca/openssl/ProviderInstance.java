package com.taoyuanx.ca.openssl;

import java.security.Provider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author 都市桃源
 * 2018年6月30日下午6:26:07
 *  提供者创建工具
 * TODO
 */
public class ProviderInstance {
	private static Provider BCProvider;
	public static Provider getBCProvider() {
		if(null==BCProvider) {
			BCProvider=new BouncyCastleProvider();
		}
		return BCProvider;
	}
	
}
