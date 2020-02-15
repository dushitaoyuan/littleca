package example.ca;

import java.io.File;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import com.taoyuanx.ca.util.CertUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.taoyuanx.ca.api.ICA;
import com.taoyuanx.ca.api.impl.CAImpl;
import com.taoyuanx.ca.bc.ProviderInstance;


/**
 * @author 都市桃源
 * 2018年6月23日下午8:25:17
 * rsa CA example 
 */
public class RSACATestExample {
	//存储基础目录
	public static final String baseCertPath="e://client/cert/rsa/";
	static {
		Security.addProvider(ProviderInstance.getBCProvider());
		try {
			File baseFileDir=new File(baseCertPath);
			File caDir=new File(baseCertPath+"ca");
			File clientDir=new File(baseCertPath+"client");
			if(!baseFileDir.exists()) {
				baseFileDir.mkdirs();
			}
			if(!caDir.exists()) {
				caDir.mkdirs();
			}
			if(!clientDir.exists()) {
				clientDir.mkdirs();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ca证书 的ca_cer_base64 地址
	static String caCert_base64=baseCertPath+"ca/ca_base64.cer";
	// ca证书的ca.cer地址
	static String caCert_cer=baseCertPath+"ca/ca.cer";
	
	// ca 公钥的ca_pub.pem地址
	static String caPublicPath=baseCertPath+"ca/ca_pub.pem";
	
	
	// 用户证书 私钥存储地址
	static String caPrivatePath=baseCertPath+"ca/ca_pri.pem";
	//用户证书的DN
	static String userDN="C=CN,ST=BJ,L=BJ,O=taoyuanx-client,OU=taoyuanx-client,CN=clients,E=lianglei_lzx@163.com";
	//签发者DN
	static String issuerDN="C=CN,ST=BJ,L=BJ,O=桃源乡,OU=桃源乡,CN=桃源乡,E=lianglei_lzx@163.com";
	// 用户 p12 存储地址
	static String caPKCS12savepath=baseCertPath+"ca/ca.p12";
	//签名算法位数
	static Integer keySize=2048;
	public static void main(String[] args) throws Exception {
		Date notBefore=new Date();
		BigInteger serialNumber=BigInteger.valueOf(1L);
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 1);
		Date notAfter=instance.getTime();
		String signHash="SHA1";
		String alg="RSA";
		testCreateCA(issuerDN, notBefore, notAfter, serialNumber, signHash, alg);
		X509Certificate CACert = CertUtil.readX509Cert(caCert_base64);
		PrivateKey privateKey = CertUtil.readPrivateKeyPem(caPrivatePath);
		testRSA(CACert, privateKey, userDN);
	}
	/**
	 * 创建初始CA
	 * @param userDN
	 * @param notBefore
	 * @param notAfter
	 * @param serialNumber
	 * @param signHash
	 * @param alg
	 * @throws Exception
	 */
	public static  void testCreateCA(String userDN, Date notBefore, Date notAfter,
			BigInteger serialNumber,String signHash,String alg) throws Exception {
		KeyPair keyPair = testCreateKeyPair();
		CertUtil.savePublicKeyPem(keyPair.getPublic(), caPublicPath);
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), caPrivatePath);
		X509Certificate makeUserSelfSignCert = CertUtil.makeUserSelfSignCert(keyPair.getPublic(), keyPair.getPrivate(), userDN, notBefore, notAfter, serialNumber, signHash.toUpperCase()+"WITH"+alg.toUpperCase());
		CertUtil.saveX509CertBase64(makeUserSelfSignCert, caCert_base64);
		CertUtil.savePKCS12(makeUserSelfSignCert, keyPair.getPrivate(), "taoyuanx-client", "123456", caPKCS12savepath);
	}
	/**
	 * 生成用户公私钥,证书
	 *  client_pri.pem 私钥文件
	 *  client_pub.pem 公钥文件
	 *   client_base64.cer base64 格式公钥证书
	 *   client.cer 二进制公钥证书
	 *    client.p12 公私钥对
	 * @param CACert
	 * @param privateKey
	 * @param userDN
	 * @throws Exception
	 */
	public static void testRSA(X509Certificate CACert, PrivateKey privateKey,String userDN) throws Exception {
		ICA ica=new CAImpl();
		ica.config(CACert,privateKey);
		KeyPair keyPair = testCreateKeyPair();
		Date notBefore=new Date();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 1);
		Date notAfter=instance.getTime();
		BigInteger serialNumber=BigInteger.valueOf(1L);
		String signAlg=CAImpl.DEFAULT_SIGN_ALG;
		X509Certificate x509Certificate = ica.makeUserCert(keyPair.getPublic(), CACert.getIssuerDN().toString(), userDN, notBefore, notAfter, serialNumber, signAlg);
		//保存
		CertUtil.saveX509CertBase64(x509Certificate, baseCertPath+"client/client_base64.cer");
		CertUtil.saveX509CertBinary(x509Certificate, baseCertPath+"client/client.cer");
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), baseCertPath+"client/client_pri.pem");
		CertUtil.savePublicKeyPem(keyPair.getPublic(), baseCertPath+"client/client_pub.pem");
		CertUtil.savePKCS12(x509Certificate, keyPair.getPrivate(), "taoyuanx-client", "123456", baseCertPath+"client/client.p12");
		
		/**
		 * 输出生成的文件
		 */
		KeyStore readKeyStore = CertUtil.readKeyStore(caPKCS12savepath, "123456");
		
		System.out.println(CertUtil.getPrivateKey(readKeyStore, "123456", "taoyuanx-client"));
		
		System.out.println(CertUtil.getPublicKey(readKeyStore, "taoyuanx-client"));
		System.out.println(x509Certificate);
		
		System.out.println(CertUtil.verifyUserCert(x509Certificate, CACert.getPublicKey()));
	}
	/**
	 * 创建密钥对生成器
	 * @return
	 * @throws Exception
	 */
	public static KeyPair testCreateKeyPair() throws Exception {
		KeyPairGenerator kpg=kpg=KeyPairGenerator.getInstance("RSA",BouncyCastleProvider.PROVIDER_NAME);
		kpg.initialize(keySize);
		KeyPair keyPair = kpg.generateKeyPair();
		return keyPair;
	}
}
