package com.taoyuanx.ca.encode.api;

public class HmacConfig {
	private String hmacAlgorithm;
	private boolean needBc=true;
	private byte[] key;
	
	public byte[] getKey() {
		return key;
	}
	public void setKey(byte[] key) {
		this.key = key;
	}
	public String getHmacAlgorithm() {
		return hmacAlgorithm;
	}
	public void setHmacAlgorithm(String hmacAlgorithm) {
		this.hmacAlgorithm = hmacAlgorithm;
	}
	public boolean isNeedBc() {
		return needBc;
	}
	public void setNeedBc(boolean needBc) {
		this.needBc = needBc;
	}
	
	
}
