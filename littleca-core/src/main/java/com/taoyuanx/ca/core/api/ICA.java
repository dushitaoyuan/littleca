package com.taoyuanx.ca.core.api;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import com.taoyuanx.ca.core.exception.CertException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

/**
 * @author 都市桃源
 * 2018年6月17日下午4:52:25
 * 证书操作接口
 */
public interface ICA {
	/**
	 * 信息设置
	 * @param publicKeyCert ROOTCA 证书
	 * @param privateKey rootCA 私钥
	 *  @param keyPair 公私钥生成器
	 */
	void config(Certificate publicKeyCert,PrivateKey privateKey) throws CertException;
	
	/** 生成公私钥对
	 * @param alg 算法
	 * @param keySize 算法位数
	 * @return
	 */
	KeyPair buildKeyPair(String alg,Integer keySize) throws CertException;
	
	KeyPair buildKeyPair(String alg) throws CertException;
	/**
	 * 保存公钥地址
	 * @param pubSavePath
	 */
	void savePublicKeyPem(PublicKey publicKey,String pubSavePath) throws CertException;
	/**
	 * 明文保存私钥地址
	 * @param priSavePath
	 */
	void savePrivateKeyPem(PrivateKey privateKey,String priSavePath) throws CertException;
	
	/**加密保存私钥地址
	 * @param priSavePath 保存地址
	 * @param password 加密密码
	 * @param pemEncryptAlg 加密算法
	 */
	void savePrivateKeyPem(PrivateKey privateKey,String priSavePath,String password,String pemEncryptAlg) throws CertException;
	
	/** 签发用户证书
	 * @param publicKey  用户公钥
	 * @param issuerDN 签发的DN
	 * @param userDN 用户证书DN
	 * @param notBefore 证书生效时间
	 * @param notAfter 证书到期时间
	 * @param serialNumber 证书序列号
	 * @param signAlg 证书签名算法
	 * @return 签名过的证书
	 * @throws CertException 
	 */
	X509Certificate makeUserCert(PublicKey publicKey,String issuerDN,String userDN,
			Date notBefore,Date notAfter,BigInteger serialNumber
			,String signAlg) throws CertException;
	/**
	 * 创建用户请求证书
	 * @param publicKey 用户公钥
	 * @param userDN 用户DN
	 * @param signAlg 签名算法
	 * @return 
	 * @throws CertException
	 */
	PKCS10CertificationRequest makeUserCertReq(PublicKey publicKey,String userDN,String signAlg) throws CertException;
	/**
	 * 验证用户证书签名
	 * @param userCert
	 * @param CAPublicKey
	 * @return
	 * @throws CertException
	 */
	boolean verifyUserCert(X509Certificate userCert,PublicKey CAPublicKey)throws CertException;

	
	
}
