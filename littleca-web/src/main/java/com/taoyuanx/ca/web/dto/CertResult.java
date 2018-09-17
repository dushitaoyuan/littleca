package com.taoyuanx.ca.web.dto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class CertResult {
	private PublicKey pub;
	private PrivateKey pri;
	private X509Certificate cert;
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
	
}
