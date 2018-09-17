package com.taoyuanx.ca.web.config;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import com.taoyuanx.ca.web.common.CAConstant.KeyType;

/**
 * @author 都市桃源
 * 2018年9月14日 下午2:08:00
 * ca 配置
*/
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
	private String defaultSignAlg;
	private KeyType caType;
	
	public String getDefaultSignAlg() {
		return defaultSignAlg;
	}
	public void setDefaultSignAlg(String defaultSignAlg) {
		this.defaultSignAlg = defaultSignAlg;
	}
	public PublicKey getPub() {
		return pub;
	}
	public void setPub(PublicKey pub) {
		this.pub = pub;
	}
	public PrivateKey getPri() {
		return pri;
	}
	public void setPri(PrivateKey pri) {
		this.pri = pri;
	}
	public X509Certificate getCert() {
		return cert;
	}
	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
	public String getIssuerDN() {
		return issuerDN;
	}
	public void setIssuerDN(String issuerDN) {
		this.issuerDN = issuerDN;
	}
	public KeyType getCaType() {
		return caType;
	}
	public void setCaType(KeyType caType) {
		this.caType = caType;
	}
	
	
}
