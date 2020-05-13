package com.taoyuanx.test;


import com.taoyuanx.ca.encode.SymmetricEncode;
import com.taoyuanx.ca.encode.api.SymmetricConfig;
import com.taoyuanx.ca.encode.eum.StringByteType;
import com.taoyuanx.ca.encode.eum.SymmetricAlgorithm;
import com.taoyuanx.ca.util.BytesStringUtil;
import org.junit.Test;

/**
 * @author 都市桃源
 * 2018年6月2日下午11:04:13
 * 对称加密测试工具
 */
public class SymmetricEncodeTest {

	
	/**
	 * 测试DES
	 * @throws Exception
	 */
	@Test
	public   void testDES() throws Exception {
		SymmetricConfig config=new SymmetricConfig();
		config.setPassword("12345678");
		config.setType(SymmetricAlgorithm.DES_ECB_PKCS5Padding.getAlgorithm());
		SymmetricEncode symmetricEncode =new SymmetricEncode(config);
		String content="1234567812345678123456781234567812345678123456781234567812345678";
		StringByteType encodeType= StringByteType.BASE64;
		String encodeContent= BytesStringUtil.toString(symmetricEncode.encode(content.getBytes()), encodeType);
		String decoedContent=new String(symmetricEncode.decode(BytesStringUtil.toBytes(encodeContent, encodeType)));
		System.out.println("加密算法类型:"+config.getType()+"\t原文:"+content+"\t 加密后:"+encodeContent+"\t解密后:"+decoedContent);
	}
	
	
	/**
	 * 测试IDEA
	 * @throws Exception
	 */
	@Test
	public   void testIDEA() throws Exception {
		SymmetricConfig config=new SymmetricConfig();
		config.setPassword("12345678");
		config.setType(SymmetricAlgorithm.IDEA_ECB_PKCS5Padding.getAlgorithm());
		config.neddBC();
		SymmetricEncode symmetricEncode =new SymmetricEncode(config);
		String content="1234567812345678123456781234567812345678123456781234567812345678";
		StringByteType encodeType= StringByteType.BASE64;
		String encodeContent=BytesStringUtil.toString(symmetricEncode.encode(content.getBytes()), encodeType);
		String decoedContent=new String(symmetricEncode.decode(BytesStringUtil.toBytes(encodeContent, encodeType)));
		System.out.println("加密算法类型:"+config.getType()+"\t原文:"+content+"\t 加密后:"+encodeContent+"\t解密后:"+decoedContent);
	}
	
	/**
	 * 测试3DES
	 * @throws Exception
	 */
	@Test
	public   void testDESede() throws Exception {
		SymmetricConfig config=new SymmetricConfig();
		config.setPassword("123456781234567812345678");
		config.setType(SymmetricAlgorithm.DESede_CBC_PKCS5Padding.getAlgorithm());
		byte[] iv=new byte[]{0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0};
		config.setIv(iv);
		SymmetricEncode symmetricEncode =new SymmetricEncode(config);
		String content="1234567812345678123456781234567812345678123456781234567812345678";
		StringByteType encodeType= StringByteType.BASE64;
		String encodeContent=BytesStringUtil.toString(symmetricEncode.encode(content.getBytes()), encodeType);
		String decoedContent=new String(symmetricEncode.decode(BytesStringUtil.toBytes(encodeContent, encodeType)));
		System.out.println("加密算法类型:"+config.getType()+"\t原文:"+content+"\t 加密后:"+encodeContent+"\t解密后:"+decoedContent);
	}
	/**
	 * 测试blowfish
	 * @throws Exception
	 */
	@Test
	public   void testBlowfish() throws Exception {
		SymmetricConfig config=new SymmetricConfig();
		config.setPassword("123456781234567812345678");
		config.setType(SymmetricAlgorithm.Blowfish_CBC_PKCS5Padding.getAlgorithm());
		byte[] iv=new byte[]{0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0};
		config.setIv(iv);
		SymmetricEncode symmetricEncode =new SymmetricEncode(config);
		String content="1234567812345678123456781234567812345678123456781234567812345678";
		StringByteType encodeType= StringByteType.BASE64;
		String encodeContent=BytesStringUtil.toString(symmetricEncode.encode(content.getBytes()), encodeType);
		String decoedContent=new String(symmetricEncode.decode(BytesStringUtil.toBytes(encodeContent, encodeType)));
		System.out.println("加密算法类型:"+config.getType()+"\t原文:"+content+"\t 加密后:"+encodeContent+"\t解密后:"+decoedContent);
	}
	/**
	 * 测试AES
	 * @throws Exception
	 */
	@Test
	public   void testAES() throws Exception {
		SymmetricConfig config=new SymmetricConfig();
		config.setPassword("1234567812345678");
		config.setType(SymmetricAlgorithm.AES_ECB_PKCS7Padding.getAlgorithm());
		byte[] iv=new byte[]{0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0};
		config.setIv(iv);
		config.neddBC();
		SymmetricEncode symmetricEncode =new SymmetricEncode(config);
		String content="12345678";
		StringByteType encodeType= StringByteType.BASE64;
		String encodeContent=BytesStringUtil.toString(symmetricEncode.encode(content.getBytes()), encodeType);
		String decoedContent=new String(symmetricEncode.decode(BytesStringUtil.toBytes(encodeContent, encodeType)));
		System.out.println("加密算法类型:"+config.getType()+"\t原文:"+content+"\t 加密后:"+encodeContent+"\t解密后:"+decoedContent);
	}
	/**
	 * 测试rc2
	 * @throws Exception
	 */
	@Test
	public   void testRC2() throws Exception {
		SymmetricConfig config=new SymmetricConfig();
		config.setPassword("1234567812345678");
		config.setType(SymmetricAlgorithm.RC2_CBC_PKCS5Padding.getAlgorithm());
		byte[] iv=new byte[]{0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0};
		config.setIv(iv);
		SymmetricEncode symmetricEncode =new SymmetricEncode(config);
		String content="12345678";
		StringByteType encodeType= StringByteType.BASE64;
		String encodeContent=BytesStringUtil.toString(symmetricEncode.encode(content.getBytes()), encodeType);
		String decoedContent=new String(symmetricEncode.decode(BytesStringUtil.toBytes(encodeContent, encodeType)));
		System.out.println("加密算法类型:"+config.getType()+"\t原文:"+content+"\t 加密后:"+encodeContent+"\t解密后:"+decoedContent);
	}
	/**
	 * 测试rc4
	 * @throws Exception
	 */
	@Test
	public   void testRC4() throws Exception {
		SymmetricConfig config=new SymmetricConfig();
		config.setPassword("1234567812345678");
		config.setType(SymmetricAlgorithm.RC4_ECB_NoPadding.getAlgorithm());
		SymmetricEncode symmetricEncode =new SymmetricEncode(config);
		String content="12345678";
		StringByteType encodeType= StringByteType.BASE64;
		String encodeContent=BytesStringUtil.toString(symmetricEncode.encode(content.getBytes()), encodeType);
		String decoedContent=new String(symmetricEncode.decode(BytesStringUtil.toBytes(encodeContent, encodeType)));
		System.out.println("加密算法类型:"+config.getType()+"\t原文:"+content+"\t 加密后:"+encodeContent+"\t解密后:"+decoedContent);
	}
	/**
	 * 测试rc4
	 * @throws Exception
	 */
	@Test
	public   void testRC5() throws Exception {
		SymmetricConfig config=new SymmetricConfig();
		config.setPassword("1234567812345678");
		config.setType(SymmetricAlgorithm.RC5_ECB_NoPadding.getAlgorithm());
		SymmetricEncode symmetricEncode =new SymmetricEncode(config);
		String content="12345678";
		StringByteType encodeType= StringByteType.BASE64;
		String encodeContent=BytesStringUtil.toString(symmetricEncode.encode(content.getBytes()), encodeType);
		String decoedContent=new String(symmetricEncode.decode(BytesStringUtil.toBytes(encodeContent, encodeType)));
		System.out.println("加密算法类型:"+config.getType()+"\t原文:"+content+"\t 加密后:"+encodeContent+"\t解密后:"+decoedContent);
	}
}
