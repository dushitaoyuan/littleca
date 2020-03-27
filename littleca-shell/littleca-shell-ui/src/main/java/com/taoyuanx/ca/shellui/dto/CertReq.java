package com.taoyuanx.ca.shellui.dto;


import com.taoyuanx.ca.shell.params.CertSubject;

public class CertReq {


    //秘钥位数
    private String keySize;

    private CertSubject subject;

    private String certPassword;

    private String certExpireDay;

	public String getKeySize() {
		return keySize;
	}

	public void setKeySize(String keySize) {
		this.keySize = keySize;
	}

	public CertSubject getSubject() {
		return subject;
	}

	public void setSubject(CertSubject subject) {
		this.subject = subject;
	}

	public String getCertPassword() {
		return certPassword;
	}

	public void setCertPassword(String certPassword) {
		this.certPassword = certPassword;
	}

	public String getCertExpireDay() {
		return certExpireDay;
	}

	public void setCertExpireDay(String certExpireDay) {
		this.certExpireDay = certExpireDay;
	}


}
