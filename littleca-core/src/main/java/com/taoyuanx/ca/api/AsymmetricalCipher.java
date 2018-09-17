package com.taoyuanx.ca.api;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 非对称加解密
 * @author 都市桃源
 * 2018年7月1日下午10:36:40
 * TODO
 */
public interface AsymmetricalCipher {
	/**
	 * RSA
	 */
	public static final String RSA_NOPADDING_CIPHER="RSA";
	public static final String RSA_PKCS1_CIPHER="RSA/PKCS1";
	public static final String RSA_PKCS1v1_5Padding_PrivateOnly_CIPHER="RSA/1";
	public static final String RSA_PKCS1v1_5Padding_PublicOnly_CIPHER="RSA/2";
	public static final String RSA_ISO9796d1Padding_CIPHER="RSA/ISO9796-1";
	public static final String RSA_OAEP_CIPHER="RSA/OAEP";
	
	/**
	 * EC
	 */
	public static final String ECIES_CIPHER="ECIES";
	public static final String ECIESwithAES_CBC_CIPHER="ECIESwithAES-CBC";
	public static final String ECIESWITHAES_CBC_CIPHER="ECIESWITHAES-CBC";
	public static final String ECIESwithDESEDE_CBC_CIPHER="ECIESwithDESEDE-CBC";
	public static final String ECIESWITHDESEDE_CBC_CIPHER="ECIESWITHDESEDE-CBC";
	
	
	public static final String SM2_CIPHER="SM2";
	/**加密解密
	 * @throws Exception 
	 * 
	 */
	byte[] encrypt(byte[] data,PublicKey publicKey) throws Exception;
	byte[] decrypt(byte[] data,PrivateKey privateKey) throws Exception;

}
