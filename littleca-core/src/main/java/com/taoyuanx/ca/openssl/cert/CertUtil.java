package com.taoyuanx.ca.openssl.cert;

import java.io.*;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.Enumeration;

import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.bc.BcPEMDecryptorProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcePEMEncryptorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import com.taoyuanx.ca.exception.CertException;
import com.taoyuanx.ca.openssl.ProviderInstance;

/**
 * @author 都市桃源 2018年6月17日下午7:04:17 TODO 证书支持工具类
 */
@SuppressWarnings("all")
public class CertUtil {
	public static final String KEYSTORE_TYPE_P12 = "PKCS12";
	public static final String KEYSTORE_TYPE_JKS = "JKS";
	static {
		Security.addProvider(ProviderInstance.getBCProvider());
	}

	/**
	 * 读取x509 证书
	 * 
	 * @param pemPath
	 * @return
	 */
	public static X509Certificate readX509Cert(String savePath) throws CertException {
		try {
			if (null == savePath) {
				throw new CertException("save path can't be null");
			}
			PEMParser pemParser = new PEMParser(new InputStreamReader(new FileInputStream(savePath)));
			Object readObject = pemParser.readObject();
			if (readObject instanceof X509CertificateHolder) {
				X509CertificateHolder holder = (X509CertificateHolder) readObject;
				return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME)
						.getCertificate(holder);
			}
			pemParser.close();
			throw new CertException(savePath + "file read format failed");
		} catch (Exception e) {
			throw new CertException("read x509 cert failed", e);
		}
	}

	/**
	 * 保存公钥证书 base64编码
	 * 
	 * @param cert
	 * @param savePath
	 * @throws Exception
	 */
	public static void saveX509CertBase64(final X509Certificate cert, String savePath) throws Exception {

		try {
			if (null == cert) {
				throw new CertException("cert can't be null");
			}
			if (null == savePath) {
				throw new CertException(" savePath can't be null");
			}
			JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(new FileWriter(savePath));
			jcaPEMWriter.writeObject(cert, null);
			jcaPEMWriter.close();
		} catch (Exception e) {
			throw new CertException("save cert failed", e);
		}
	}

	/**
	 * 保存公钥证书 二进制格式
	 * 
	 * @param cert
	 * @param savePath
	 * @throws Exception
	 */
	public static void saveX509CertBinary(X509Certificate cert, String savePath) throws Exception {

		try {
			if (null == savePath) {
				throw new CertException("savePath can't be null");
			}
			FileOutputStream stream = new FileOutputStream(savePath);
			stream.write(cert.getEncoded());
			stream.close();
		} catch (Exception e) {
			throw new CertException("save x509 cert failed", e);
		}

	}

	/**
	 * 明文保存 publicKey
	 * 
	 * @param publicKey
	 * @param savePath
	 * @throws CertException
	 */
	public static void savePublicKeyPem(final PublicKey publicKey, String savePath) throws CertException {
		try {
			if (null == publicKey) {
				throw new CertException("publicKey can't be null");
			}
			if (null == savePath) {
				throw new CertException(" savePath can't be null");
			}
			JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(new FileWriter(savePath));
			jcaPEMWriter.writeObject(publicKey);
			jcaPEMWriter.close();
		} catch (Exception e) {
			throw new CertException("save publicKey failed", e);
		}
	}

	/**
	 * 读取pem 格式公钥
	 * 
	 * @param pemPath
	 * @return
	 */
	public static PublicKey readPublicKeyPem(String publicKeyPath) throws CertException {
		try {
			if (null == publicKeyPath) {
				throw new CertException("publicKeyPath can't be null");
			}
			PEMParser pemParser = new PEMParser(new FileReader(new File(publicKeyPath)));
				//	(new InputStreamReader(new FileInputStream(publicKeyPath)));
			Object readObject = pemParser.readObject();
			if (readObject instanceof SubjectPublicKeyInfo) {
				AsymmetricKeyParameter createKey = PublicKeyFactory.createKey((SubjectPublicKeyInfo) readObject);
				if (createKey.getClass().equals(RSAKeyParameters.class)) {// RSA
					RSAKeyParameters publicKeyParameters = (RSAKeyParameters) createKey;
					KeyFactory kf = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
					return kf.generatePublic(
							new RSAPublicKeySpec(publicKeyParameters.getModulus(), publicKeyParameters.getExponent()));
				} else if (createKey.getClass().equals(DSAPublicKeyParameters.class)) {// DSA
					DSAPublicKeyParameters publicKeyParameters = (DSAPublicKeyParameters) createKey;
					KeyFactory kf = KeyFactory.getInstance("DSA", BouncyCastleProvider.PROVIDER_NAME);
					DSAParameters parameters = publicKeyParameters.getParameters();
					return kf.generatePublic(new DSAPublicKeySpec(publicKeyParameters.getY(), parameters.getP(),
							parameters.getQ(), parameters.getG()));
				} else if (createKey.getClass().equals(ECPublicKeyParameters.class)) {// ECC
					ECPublicKeyParameters publicKeyParameters = (ECPublicKeyParameters) createKey;
					KeyFactory kf = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
					ECDomainParameters parameters = publicKeyParameters.getParameters();
					ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(publicKeyParameters.getQ(),
							new ECParameterSpec(parameters.getCurve(), parameters.getG(), parameters.getN(),
									parameters.getH(), parameters.getSeed()));
					return kf.generatePublic(ecPublicKeySpec);
				}
			}
			pemParser.close();
			throw new CertException(publicKeyPath + "file read format failed");
		} catch (Exception e) {
			throw new CertException("read x509 cert failed", e);
		}
	}

	/**
	 * 明文保存 privateKey
	 * 
	 * @param privateKey
	 * @param savePath
	 * @throws CertException
	 */
	public static void savePrivateKeyPem(final PrivateKey privateKey, String savePath) throws CertException {
		try {
			if (null == privateKey) {
				throw new CertException("privateKey can't be null");
			}
			if (null == savePath) {
				throw new CertException(" savePath can't be null");
			}
			JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(new FileWriter(savePath));
			jcaPEMWriter.writeObject(privateKey);
			jcaPEMWriter.close();
		} catch (Exception e) {
			throw new CertException("save privateKey failed", e);
		}
	}

	/**
	 * 不支持sm2
	 * 
	 * @param privateKey
	 *            私钥
	 * @param savePath
	 *            保存路径
	 * @param password
	 *            加密保存密码
	 * @param encryptType
	 *            加密类型 默认DES-EDE3-CBC
	 * @throws CertException
	 */
	public static void savePrivateKeyPem(PrivateKey privateKey, String savePath, String password, String encryptType)
			throws CertException {
		try {
			if (null == privateKey) {
				throw new CertException("privateKey can't be null");
			}
			if (null == password) {
				throw new CertException("password can't be null");
			}
			if (null == savePath) {
				throw new CertException("savePath can't be null");
			}
			if (null == encryptType) {
				encryptType = "DES-EDE3-CBC";
			}
			JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(new FileWriter(savePath));
			jcaPEMWriter.writeObject(privateKey, new JcePEMEncryptorBuilder(encryptType).build(password.toCharArray()));
			jcaPEMWriter.close();
		} catch (Exception e) {
			throw new CertException("save privateKey failed", e);
		}
	}

	/**
	 * 明文pem格式私钥读取
	 * 
	 * @param privateKeyPemPath
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey readPrivateKeyPem(String privateKeyPemPath) throws CertException {
		try {
			PEMParser pemParser = new PEMParser(new InputStreamReader(new FileInputStream(privateKeyPemPath)));
			Object readObject = pemParser.readObject();
			if (readObject instanceof PEMKeyPair) {
				PEMKeyPair key = (PEMKeyPair) readObject;
				return new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getKeyPair(key)
						.getPrivate();
			}

			throw new CertException("read privateKey failed");
		} catch (Exception e) {
			throw new CertException("read privateKey failed", e);
		}
	}

	/**
	 * 密文pem格式私钥读取
	 * 
	 * @param privateKeyPemPath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey readPrivateKeyPem(String privateKeyPemPath, String password) throws CertException {
		try {
			if (null == password) {
				throw new CertException("password can't be null ");
			}
			PEMParser pemParser = new PEMParser(new InputStreamReader(new FileInputStream(privateKeyPemPath)));
			Object readObject = pemParser.readObject();
			if (readObject instanceof PEMEncryptedKeyPair) {
				PEMEncryptedKeyPair keyPair = (PEMEncryptedKeyPair) readObject;
				PEMDecryptorProvider keyDecryptorProvider = new BcPEMDecryptorProvider(password.toCharArray());
				PEMKeyPair decryptKeyPair = keyPair.decryptKeyPair(keyDecryptorProvider);
				return new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME)
						.getKeyPair(decryptKeyPair).getPrivate();
			}
			throw new CertException("read privateKey failed");
		} catch (Exception e) {
			throw new CertException("read privateKey failed", e);
		}
	}

	/**
	 * 生成p12 格式用户证书
	 * 
	 * @param userCert
	 * @param userPrivateKey
	 * @param CACert
	 * @param userAlias
	 * @param password
	 * @param savePath
	 * @throws CertException
	 */
	public static void savePKCS12(X509Certificate userCert, PrivateKey userPrivateKey, String userAlias,
			String password, String savePath) throws CertException {
		try {
			if (userAlias == null) {
				throw new CertException("userAlias can't be null");
			}
			if (password == null) {
				throw new CertException("password can't be null");
			}
			if (savePath == null) {
				throw new CertException("savePath can't be null");
			}
			if (userCert == null) {
				throw new CertException("userCert can't be null");
			}
			if (userPrivateKey == null) {
				throw new CertException("userPrivateKey can't be null");
			}
			KeyStore keyStore = KeyStore.getInstance("PKCS12", BouncyCastleProvider.PROVIDER_NAME);
			keyStore.load(null, null);
			keyStore.setKeyEntry(userAlias, userPrivateKey, password.toCharArray(), new X509Certificate[] { userCert });
			FileOutputStream fos = new FileOutputStream(new File(savePath));
			keyStore.store(fos, password.toCharArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			throw new CertException("read pkcs12 failed", e);
		}
	}

	/**
	 * 验证用户证书签名合法性
	 * 
	 * @param userCert
	 * @param CAPublicKey
	 * @return
	 * @throws CertException
	 */
	public static boolean verifyUserCert(X509Certificate userCert, PublicKey CAPublicKey) throws CertException {
		try {
			if (userCert == null) {
				throw new CertException("userCert can't be null");
			}
			if (CAPublicKey == null) {
				throw new CertException("CAPublicKey can't be null");
			}
			userCert.verify(CAPublicKey);
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	/**
	 * 根据文件名称判断秘钥类型
	 * 
	 * @param filePath
	 * @return
	 */
	public static String guessKeystoreType(String filePath) {
		String suffix = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
		if (suffix.equals("jks") || suffix.equals("keystore")) {
			return KEYSTORE_TYPE_JKS;
		} else if (suffix.equals("p12")) {
			return KEYSTORE_TYPE_P12;
		}
		return KEYSTORE_TYPE_P12;
	}



	public static KeyStore readKeyStore(String filePath, String keyPassword) throws CertException {
		try {
			KeyStore keyStore = KeyStore.getInstance(guessKeystoreType(filePath), BouncyCastleProvider.PROVIDER_NAME);
			FileInputStream file = new FileInputStream(new File(filePath));
			keyStore.load(file, keyPassword.toCharArray());
			return keyStore;
		} catch (Exception e) {
			throw new CertException("read KeyStore failed", e);
		}
	}
	public static KeyStore readKeyStore(InputStream inputStream, String keyStoreType,String keyPassword) throws CertException {
		try {
			KeyStore keyStore = KeyStore.getInstance(keyStoreType, BouncyCastleProvider.PROVIDER_NAME);
			keyStore.load(inputStream, keyPassword.toCharArray());
			return keyStore;
		} catch (Exception e) {
			throw new CertException("read KeyStore failed", e);
		}
	}
	public static PublicKey getPublicKey(KeyStore keyStore, String alias) throws CertException {
		try {
			Enumeration<String> aliases = keyStore.aliases();
			while (aliases.hasMoreElements()) {
				System.out.println(aliases.nextElement());
			}
			Certificate certificate = keyStore.getCertificate(alias);
			if (certificate == null) {
				throw new CertException(alias + " alias not found");
			}
			return certificate.getPublicKey();
		} catch (Exception e) {
			throw new CertException("analyze KeyStore failed", e);
		}
	}

	public static PrivateKey getPrivateKey(KeyStore keyStore, String keyPassword, String alias) throws CertException {
		try {
			if (!keyStore.isKeyEntry(alias)) {
				throw new CertException(alias + " alias not found");
			}
			return (PrivateKey) keyStore.getKey(alias, keyPassword.toCharArray());
		} catch (Exception e) {
			throw new CertException("analyze KeyStore failed", e);
		}
	}

	/**
	 * 创建一个自签名的证书
	 * 
	 * @param publicKey
	 * @param privateKey
	 * @param userDN
	 * @param notBefore
	 * @param notAfter
	 * @param serialNumber
	 * @param signAlg
	 * @return
	 * @throws CertException
	 */
	public static X509Certificate makeUserSelfSignCert(PublicKey publicKey, PrivateKey privateKey, String userDN,
			Date notBefore, Date notAfter, BigInteger serialNumber, String signAlg) throws CertException {
		try {
			if (null == signAlg) {
				throw new CertException(signAlg + " can't be null");
			}
			X500Name issuer = new X500Name(userDN);
			//1. 创建签名
			ContentSigner signer = new JcaContentSignerBuilder(signAlg)
					.setProvider(BouncyCastleProvider.PROVIDER_NAME).build(privateKey);
			//2. 创建证书请求
			PKCS10CertificationRequestBuilder pkcs10CertificationRequestBuilder = new JcaPKCS10CertificationRequestBuilder(issuer,publicKey);
			PKCS10CertificationRequest pkcs10CertificationRequest = pkcs10CertificationRequestBuilder.build(signer);
			
			//3. 创建证书
			//SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
			X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuer, serialNumber,
					notBefore, notAfter, pkcs10CertificationRequest.getSubject(), pkcs10CertificationRequest.getSubjectPublicKeyInfo());
			
			//添加扩展信息 见 X509CertExtensions
			X509CertExtensions.buildAllExtensions(certBuilder, publicKey, publicKey);
			X509CertificateHolder holder = certBuilder.build(signer);
			return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME)
					.getCertificate(holder);

		} catch (Exception e) {
			throw new CertException("makeUserSelfSignCert failed", e);
		}
	}

	/**
	 * 创建ca私钥签名证书
	 * 
	 * @param publicKey
	 * @param privateKey
	 * @param issuerDN
	 * @param userDN
	 * @param notBefore
	 * @param notAfter
	 * @param serialNumber
	 * @param signAlg
	 * @return
	 * @throws CertException
	 */
	public static X509Certificate makeUserCert(PublicKey publicKey,PublicKey caPublicKey, PrivateKey caPrivateKey, String issuerDN,
			String userDN, Date notBefore, Date notAfter, BigInteger serialNumber, String signAlg)
			throws CertException {
		try {
			if (null == signAlg) {
				throw new CertException(signAlg + " can't be null");
			}
			
			X500Name issuer = new X500Name(issuerDN);
			//1. 创建签名
			ContentSigner signer = new JcaContentSignerBuilder(signAlg)
					.setProvider(BouncyCastleProvider.PROVIDER_NAME).build(caPrivateKey);
			//2. 创建证书请求
			PKCS10CertificationRequestBuilder pkcs10CertificationRequestBuilder = new JcaPKCS10CertificationRequestBuilder(new X500Name(userDN),publicKey);
			PKCS10CertificationRequest pkcs10CertificationRequest = pkcs10CertificationRequestBuilder.build(signer);
			//3. 创建证书
			//SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
			
			SubjectPublicKeyInfo subPubKeyInfo = pkcs10CertificationRequest.getSubjectPublicKeyInfo();
			X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuer, serialNumber,
					notBefore, notAfter, pkcs10CertificationRequest.getSubject(), subPubKeyInfo);
			//添加扩展信息 见 X509CertExtensions
			X509CertExtensions.buildAllExtensions(certBuilder, publicKey, caPublicKey);
			X509CertificateHolder holder = certBuilder.build(signer);
			return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME)
					.getCertificate(holder);
		} catch (Exception e) {
			throw new CertException("makeUserCert failed", e);
		}
	}

}
