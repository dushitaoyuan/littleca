package com.taoyuanx.ca.token;


import com.taoyuanx.ca.util.JwtJsonUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 都市桃源
 * 2018年5月23日下午10:19:11
 *  token实体
 */
@SuppressWarnings("all")
public class Token {
	/**
	 * jwt token格式
	 * base64url_encode(Header) + '.' + base64url_encode(Claims) + '.' + base64url_encode(Signature)
	 * 
	 * typ 类型 alg 算法 iss token的签发者 sub token所面向的用户,
	 * aud 接收token的一方, 
	 * exp过期时间 iat 创建时间 jti token唯一ID
	 * nbf token 生效时间
	 * pri 自定义 加密键值对
	 * pub 自定义公开键值对
	 */
	private String typ;
	private String alg;
	private String iss;
	private String sub;
	private Long exp;
	private Long iat;
	private String jti;
	private String[] aud;
	private Long nbf;

	private Map<String,String> pri;
	private Map<String,String> pub;

	
	public void cliamsPub(String name,Object value) {
		if(null==pub) {
			pub=new HashMap<>(); 
		}
		doPutValue(name, value, pub);
	}
	public <T> T getPubCliams(String name,Class<T> clazz) {
		if(null==pub) {
			return null;
		}
		return doGetValue(name, pub, clazz);
	}
	public void cliamsPri(String name,Object value) {
		if(null==pri) {
			pri=new HashMap<>(); 
		}
		doPutValue(name, value, pri);
	}
	public <T> T getPriCliams(String name,Class<T> clazz) {
		if(null==pri) {
			return null;
		}
		return doGetValue(name, pri, clazz);
	}

	private void doPutValue(String key,Object value,Map<String,String>  map) {
		if(null==value) {
			return;
		}
		if(value instanceof String) {
			map.put(key, (String) value);
		}
		if(value instanceof Number) {
			map.put(key, value.toString());
		}
		if(value instanceof Boolean) {
			map.put(key,value.toString());
		}
		if(value instanceof Date) {
			map.put(key, String.valueOf(((Date) value).getTime()));
		}
		map.put(key, JwtJsonUtil.serialize(value));
	}
    private <T> T doGetValue(String key,Map<String,String>  map,Class<T> clazz) {
    	if(!map.containsKey(key)) {
    		return null;
    	}
    	String value=map.get(key);
    	if(String.class.equals(clazz)) {
    		return (T) value; 
    	}
    	if(Number.class.equals(clazz)) {
    		Double d = Double.parseDouble(value);
    		if(Integer.class.equals(clazz)) {
    			return (T) (Integer)d.intValue();
    		}
    		if(Long.class.equals(clazz)) {
    			return (T) (Long)d.longValue();
    		}
    		if(Float.class.equals(clazz)) {
    			return (T) (Float)d.floatValue();
    		}
    		if(Byte.class.equals(clazz)) {
    			return (T) (Byte)d.byteValue();
    		}
    		return (T) d;
    		
    	}
    	if(Boolean.class.equals(clazz)) {
    		return (T) (Boolean)Boolean.valueOf(value);
    	}
    	if(Date.class.equals(clazz)) {
    		return (T) new Date(Long.parseLong(value));
    	}
		return JwtJsonUtil.deserialize(value, clazz);
	}
	
	
	
	public Map<String, String> getPri() {
		return pri;
	}
	public void setPri(Map<String, String> pri) {
		this.pri = pri;
	}
	public Map<String, String> getPub() {
		return pub;
	}
	public void setPub(Map<String, String> pub) {
		this.pub = pub;
	}
	public Long getNbf() {
		return nbf;
	}
	public void setNbf(Long nbf) {
		this.nbf = nbf;
	}
	public String[] getAud() {
		return aud;
	}
	public void setAud(String[] aud) {
		this.aud = aud;
	}
	
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	public String getAlg() {
		return alg;
	}
	public void setAlg(String alg) {
		this.alg = alg;
	}
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public Long getExp() {
		return exp;
	}
	public void setExp(Long exp) {
		this.exp = exp;
	}
	public Long getIat() {
		return iat;
	}
	public void setIat(Long iat) {
		this.iat = iat;
	}
	public String getJti() {
		return jti;
	}
	public void setJti(String jti) {
		this.jti = jti;
	}
	@Override
	public String toString() {
		return "Token [typ=" + typ + ", alg=" + alg + ", iss=" + iss + ", sub=" + sub + ", exp=" + exp + ", iat=" + iat
				+ ", jti=" + jti + ", aud=" + Arrays.toString(aud) + ", nbf=" + nbf + ", pri=" + pri + ", pub=" + pub
				+ "]";
	}
	
	
}
