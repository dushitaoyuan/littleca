package com.taoyuanx.ca.web.dto;

import java.util.Date;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

public class CertReq {
	/**
	 * C 国家代码
	 * ST 省份
	 * L 城市
	 * O 组织 
	 * OU 单位  
	 * CN 用户名(可以为域名)（example:*.baidu.com）
	 * E 邮箱
	 * notBefore 证书生效时间
	 * notAfter 证书到期时间
	 * signAlg 签名算法
	 * serialNumber 证书序列号
	 */
	private String E;
	private String CN;
	private String OU;
	private String O;
	private String L;
	private String ST;
	private String C;
	@JSONField(format="yyyy-MM-dd")
	private Date notBefore;
	@JSONField(format="yyyy-MM-dd")
	private Date notAfter;
	private String signAlg;
	private String serialNumber;
	//秘钥位数
	private String keySize;
	
	public String getE() {
		return E;
	}
	public void setE(String e) {
		E = e;
	}
	public String getCN() {
		return CN;
	}
	public void setCN(String cN) {
		CN = cN;
	}
	public String getOU() {
		return OU;
	}
	public void setOU(String oU) {
		OU = oU;
	}
	public String getO() {
		return O;
	}
	public void setO(String o) {
		O = o;
	}
	public String getL() {
		return L;
	}
	public void setL(String l) {
		L = l;
	}
	
	public String getST() {
		return ST;
	}
	public void setST(String sT) {
		ST = sT;
	}
	public String getC() {
		return C;
	}
	public void setC(String c) {
		C = c;
	}
	public Date getNotBefore() {
		if(null==notBefore){
			return new Date();
		}
		return notBefore;
	}
	public void setNotBefore(Date notBefore) {
		this.notBefore = notBefore;
	}
	public Date getNotAfter() {
		return notAfter;
	}
	public void setNotAfter(Date notAfter) {
		this.notAfter = notAfter;
	}
	public String getSignAlg() {
		return signAlg;
	}
	public void setSignAlg(String signAlg) {
		this.signAlg = signAlg;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getUserDN(){
	//	"C=CN,ST=BJ,L=BJ,O=taoyuanx-client,OU=taoyuanx-client,CN=*.taoyuanx.com,E=xx@xx.com";
		StringBuilder buf=new StringBuilder();
		if(StringUtils.hasText(C)){
			buf.append("C=").append(C).append(",");
		}
		if(StringUtils.hasText(ST)){
			buf.append("ST=").append(ST).append(",");
		}
		if(StringUtils.hasText(L)){
			buf.append("L=").append(L).append(",");
		}
		if(StringUtils.hasText(O)){
			buf.append("O=").append(O).append(",");
		}
		if(StringUtils.hasText(OU)){
			buf.append("OU=").append(OU).append(",");
		}
		if(StringUtils.hasText(E)){
			buf.append("E=").append(E).append(",");
		}
		return buf.substring(0,buf.length()-1);
	}
	public String getKeySize() {
		return keySize;
	}
	public void setKeySize(String keySize) {
		this.keySize = keySize;
	}
	
}
