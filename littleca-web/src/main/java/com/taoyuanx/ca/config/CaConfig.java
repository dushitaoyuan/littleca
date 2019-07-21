package com.taoyuanx.ca.config;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import lombok.Data;

/**
 * @author 都市桃源
 * 2018年9月14日 下午2:08:00
 * ca 配置
*/
@Data
public class CaConfig {
	/**
	 * pub 公钥
	 * pri 私钥
	 * issuerDN 签发者dn
	 * defaultSignAlg 默认签名算法
	 * caType ca类型参见 CAConstant
	 */
	private PublicKey pub;
	private PrivateKey pri;
	private X509Certificate cert;
	private String issuerDN;

	/**
	 * certPath 根证书地址
	 * priPath 根私钥地址
	 * clientCertBasePath 签发客户端证书存储地址
	 * signAlg 默认签名算法
	 */
	private String certPath;
	private String priPath;
	private String clientCertBasePath;
	private String signAlg;

	
}
