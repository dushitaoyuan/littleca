package com.taoyuanx.ca.api.impl;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import com.taoyuanx.ca.bc.ProviderInstance;
import com.taoyuanx.ca.util.CertUtil;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;

import com.taoyuanx.ca.api.ICA;
import com.taoyuanx.ca.exception.CertException;
import com.taoyuanx.ca.util.Util;

/**
 * @author 都市桃源
 * 2018年6月17日下午4:52:25
 * 证书操作 实现
 */
public class CAImpl implements ICA {
	private Certificate publicKeyCert;
	private PrivateKey privateKey;
	private static final String DEFAULT_ALG="RSA";
	private static final Integer DEFAULT_KEYSIZE=1024;
    public static final String DEFAULT_SIGN_ALG="SHA1withRSA";
    public static final String CERT_TYPE="X509";
    
	static {
		Security.addProvider(ProviderInstance.getBCProvider());  
	}
	@Override
	public void config(Certificate publicKeyCert, PrivateKey privateKey) throws CertException {
		this.publicKeyCert=publicKeyCert;
		this.privateKey=privateKey;		
	}
	@Override
	public KeyPair buildKeyPair(String alg, Integer keySize) throws CertException {
		try {
			KeyPairGenerator kpg=null;
			if(Util.isEmpty(alg)) {
				kpg=KeyPairGenerator.getInstance(DEFAULT_ALG,BouncyCastleProvider.PROVIDER_NAME);
			}else{
				kpg=KeyPairGenerator.getInstance(alg.toUpperCase());
			}
			if(null==keySize) {
				kpg.initialize(DEFAULT_KEYSIZE);
			}else {
				kpg.initialize(keySize);
			}
			return kpg.generateKeyPair();
		} catch (Exception e) {
			throw new CertException("generate keypair failed", e);
		}
	}
	
	@Override
	public KeyPair buildKeyPair(String alg) throws CertException {
		try {
			KeyPairGenerator kpg=null;
			if(Util.isEmpty(alg)) {
				kpg=KeyPairGenerator.getInstance(DEFAULT_ALG,BouncyCastleProvider.PROVIDER_NAME);
			}else{
				kpg=KeyPairGenerator.getInstance(alg.toUpperCase());
			}
			return kpg.generateKeyPair();
		} catch (Exception e) {
			throw new CertException("generate keypair failed", e);
		}
	}

	@Override
	public void savePublicKeyPem(PublicKey publicKey, String pubSavePath) throws CertException {
		CertUtil.savePublicKeyPem(publicKey, pubSavePath);
	}

	@Override
	public void savePrivateKeyPem(PrivateKey privateKey, String priSavePath) throws CertException {
		CertUtil.savePrivateKeyPem(privateKey, priSavePath);
	}

	@Override
	public void savePrivateKeyPem(PrivateKey privateKey, String priSavePath, String password, String pemEncryptAlg)
			throws CertException {
		CertUtil.savePrivateKeyPem(privateKey, priSavePath, password, pemEncryptAlg);
	}

	
	public Certificate getPublicKeyCert() {
		return publicKeyCert;
	}


	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	@Override
	public X509Certificate makeUserCert(PublicKey publicKey, String issuerDN, String userDN, Date notBefore, Date notAfter,
			BigInteger serialNumber,String signAlg)  throws CertException {
			return CertUtil.makeUserCert(publicKey,publicKeyCert.getPublicKey(), privateKey, issuerDN, userDN, notBefore, notAfter, serialNumber, signAlg);	
		
	}
	@Override
	public PKCS10CertificationRequest makeUserCertReq(PublicKey publicKey, String userDN, String signAlg) throws CertException {
	    try {
	        PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(new X500Name(userDN)
	                ,SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()));
	        if(null==signAlg) {
	        	signAlg=DEFAULT_SIGN_ALG;
	        }
	        JcaContentSignerBuilder jcaBuilder = new JcaContentSignerBuilder(signAlg);
	        jcaBuilder.setProvider(BouncyCastleProvider.PROVIDER_NAME);
	        ContentSigner contentSigner = jcaBuilder.build(privateKey);
	        PKCS10CertificationRequest certificationRequest = builder.build(contentSigner);
	        return certificationRequest;
	    } catch (Exception e) {
	    	throw new CertException("makeUserCertReq failed",e);
	    } 
	}
	@Override
	public boolean verifyUserCert(X509Certificate userCert, PublicKey CAPublicKey) throws CertException {
		return  CertUtil.verifyUserCert(userCert, CAPublicKey);
	}

	
}
