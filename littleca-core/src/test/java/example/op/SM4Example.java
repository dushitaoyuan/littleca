package example.op;

import com.taoyuanx.ca.sm.util.Sm4Util;
import com.taoyuanx.ca.util.ByteStringUtil;

public class SM4Example {
	public static void main(String[] args) throws Exception {
		byte[] key = Sm4Util.generateKey();
		byte[] iv="0000000000000000".getBytes();
		String s="1234567812345678";
		System.out.println("原文:"+s);
		testEncryptAndDecrypt(key, iv, s.getBytes());
	}
	public static void testEncryptAndDecrypt(byte[] key,byte[] iv,byte[] data) throws Exception{
		 byte[] encrypt_Cbc_Padding = Sm4Util.encrypt_Cbc_Padding(key, iv, data);
		 int type=ByteStringUtil.BASE64;
		 String str=ByteStringUtil.toString(encrypt_Cbc_Padding, type);
		 byte[] decrypt_Cbc_Padding = Sm4Util.decrypt_Cbc_Padding(key, iv, encrypt_Cbc_Padding);
		 System.out.println("\n密文:"+str+"\n解密:"+new String(decrypt_Cbc_Padding));
	}
}
