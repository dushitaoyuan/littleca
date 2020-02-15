package com.taoyuanx.ca.api;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 非对称加解密
 * @author 都市桃源
 * 2018年7月1日下午10:36:40
 */
public interface AsymmetricalCipher {
	/**
	 * RSA
	 */
	String RSA_NOPADDING_CIPHER="RSA";
	String RSA_PKCS1_CIPHER="RSA/PKCS1";
	String RSA_PKCS1v1_5Padding_PrivateOnly_CIPHER="RSA/1";
	String RSA_PKCS1v1_5Padding_PublicOnly_CIPHER="RSA/2";
	String RSA_ISO9796d1Padding_CIPHER="RSA/ISO9796-1";
	String RSA_OAEP_CIPHER="RSA/OAEP";
	
	/**
	 * EC
	 */
	String ECIES_CIPHER="ECIES";
	String ECIESwithAES_CBC_CIPHER="ECIESwithAES-CBC";
	String ECIESWITHAES_CBC_CIPHER="ECIESWITHAES-CBC";
	String ECIESwithDESEDE_CBC_CIPHER="ECIESwithDESEDE-CBC";
	String ECIESWITHDESEDE_CBC_CIPHER="ECIESWITHDESEDE-CBC";
	
	
	String SM2_CIPHER="SM2";
	/**加密解密
	 * @throws Exception 
	 * 
	 */
	byte[] encrypt(byte[] data,PublicKey publicKey) throws Exception;
	byte[] decrypt(byte[] data,PrivateKey privateKey) throws Exception;

}
