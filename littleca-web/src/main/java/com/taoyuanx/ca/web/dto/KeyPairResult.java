package com.taoyuanx.ca.web.dto;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.util.StringUtils;

import com.taoyuanx.ca.web.util.KeyPairUtil;

public class KeyPairResult {
	private String pub_pem;
	private String pri_pem;
	
	
	private PublicKey pub;
	private PrivateKey pri;
	
	
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
	public String getPub_pem() {
		if(StringUtils.isEmpty(pub_pem)){
			pub_pem=KeyPairUtil.convertPublicKeyToPemString(pub);
		}
		return pub_pem;
	}
	public void setPub_pem(String pub_pem) {
		this.pub_pem = pub_pem;
	}
	public String getPri_pem() {
		if(StringUtils.isEmpty(pri_pem)){
			pri_pem=KeyPairUtil.convertPrivateKeyToPemString(pri);
		}
		return pri_pem;
	}
	public void setPri_pem(String pri_pem) {
		this.pri_pem = pri_pem;
	}
	
}
