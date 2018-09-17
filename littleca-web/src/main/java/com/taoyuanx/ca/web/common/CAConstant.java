package com.taoyuanx.ca.web.common;

public class CAConstant {
/*	public static final String RSA="RSA";
	public static final String SM2="SM2";
	public static final String ECDSA="ECDSA";
	public static final String DSA="DSA";*/
	
	public enum KeyType{
		RSA("RSA",1),SM2("SM2",2),ECDSA("ECDSA",3),DSA("DSA",4);
		public String name;
		public Integer value;
		private KeyType(String name, Integer value) {
			this.name = name;
			this.value = value;
		}
		
		public static KeyType forValue(Integer value) {
			KeyType[] values = KeyType.values();
			for(KeyType v:values) {
				if(v.value.equals(value)) {
					return v;
				}
			}
			return null;
		}
		
	}
	
}
