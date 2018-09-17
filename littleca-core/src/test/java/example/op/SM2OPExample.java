package example.op;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;

import com.taoyuanx.ca.api.AsymmetricalCipher;
import com.taoyuanx.ca.api.AsymmetricalSignature;
import com.taoyuanx.ca.api.impl.SM2;
import com.taoyuanx.ca.openssl.cert.CertUtil;
import com.taoyuanx.ca.util.ByteStringUtil;

/**
 * rsa 操作示例
 * @author 都市桃源
 * 2018年7月1日下午11:27:27
 * TODO
 */
public class SM2OPExample {
	public static void main(String[] args) throws Exception {
		//存储基础目录
		 String client="l://sm2/ca证书/cert/sm2/client/";
		 PrivateKey privateKey = CertUtil.readPrivateKeyPem(client+"client_pri.pem");
		 PublicKey publicKey = CertUtil.readPublicKeyPem(client+"client_pub.pem");
		 String data=randomData(300);
		 String signAlgorithm="SM3WITHSM2";
		 testOP(publicKey, privateKey, data,signAlgorithm);
	}
	public static void testOP(PublicKey publicKey,PrivateKey privateKey,String data,String signAlgorithm) throws Exception {
		AsymmetricalCipher op=new SM2();
		byte[] srcData=data.getBytes();
		byte[] encrypt = op.encrypt(srcData, publicKey);
		String enData=ByteStringUtil.toString(encrypt, ByteStringUtil.BASE64);
		String deData=new String(op.decrypt(encrypt, privateKey));
		AsymmetricalSignature as=(AsymmetricalSignature) op;
		byte[] sign = as.sign(data.getBytes(), privateKey, signAlgorithm);
		String signData=ByteStringUtil.toString(sign, ByteStringUtil.BASE64);
		boolean verifySign = as.verifySign(sign, data.getBytes(), publicKey, signAlgorithm);
		System.out.println("原文:"+data);
		System.out.println("密文:"+enData);
		System.out.println("解密后:"+deData);
		System.out.println("签名:"+signData);
		System.out.println("验签结果:"+verifySign);
		System.out.println("原文长度:"+srcData.length+"密文长度:"+encrypt.length);
	}
	public static  char[] ch= {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','l','r','s','t','u','v'
			,'w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','L','R','S','T','U','V'
			,'W','X','Y','Z'/*,'企','附','牲','毅','门','床','朋','祥','调','怕','胞','捞','拣','酒','卜'
			,'厘','垄','里','叶','试','溉','理','曲','壳','灭','国','昂','症','亏','恼'*/};
	/**
	 * 中文一个字符3个字节
	 * @param len
	 * @return
	 */
	public static String randomData(int len) {
		int size=ch.length;
		Random random = new Random();
		StringBuilder buf=new StringBuilder();
		for(int i=0;i<len;i++) {
			buf.append(ch[random.nextInt(size)]);
		}
		return buf.toString();
		
	}
}
