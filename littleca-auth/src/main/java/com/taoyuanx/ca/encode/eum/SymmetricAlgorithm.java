package com.taoyuanx.ca.encode.eum;

/**
 * @author 都市桃源 2018年6月2日下午10:26:37
 * 对称加密算法枚举
 * 
 *         对称密码算法参考 https://www.cnblogs.com/qq2052702900/p/5812766.html
 */
public enum SymmetricAlgorithm {
	DES_ECB_PKCS5Padding("DES/ECB/PKCS5Padding"),
	DES_CBC_PKCS5Padding("DES/CBC/PKCS5Padding"),
	
	DESede_ECB_PKCS5Padding("DESede/ECB/PKCS5Padding"),
	DESede_CBC_PKCS5Padding("DESede/CBC/PKCS5Padding"),
	
	
	AES_ECB_PKCS5Padding("AES/ECB/PKCS5Padding"),
	AES_ECB_PKCS7Padding("AES/ECB/PKCS7Padding"),
	AES_CBC_PKCS5Padding("AES/CBC/PKCS5Padding"),
	
	
	Blowfish_CBC_PKCS5Padding("Blowfish/CBC/PKCS5Padding"),
	
	RC2_CBC_PKCS5Padding("RC2/CBC/PKCS5Padding"),
	
	
	RC4_ECB_NoPadding("RC4/ECB/NoPadding"),
	
	RC5_ECB_NoPadding("RC5/ECB/NoPadding"),
	
	/**
	 * BC 支持
	 */
	IDEA_ECB_PKCS5Padding("IDEA/ECB/PKCS5Padding");
	
	private String algorithm;
	private SymmetricAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	

}
