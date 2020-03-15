package com.taoyuanx.ca.ui.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import com.taoyuanx.ca.ui.ex.SecurityException;

/**
 * @author 都市桃源
 * time:2018 下午4:25:55
 * usefor: hmac token算法 单向 简单实现
 * 
 * 
 */
public class SimpleTokenManager {
	private HmacUtils hmac;
	public SimpleTokenManager(String hmacKey) {
		this.hmac=new HmacUtils(HmacAlgorithms.HMAC_MD5, hmacKey);
	}
	public  String createToken(Map<String,String> data, Long time,TimeUnit timeUnit) {
		Long end=System.currentTimeMillis()+timeUnit.toMillis(time);
		data.put("end", String.valueOf(end));
		String signData=mapToString(data);
		String sign = Base64.encodeBase64URLSafeString(hmac.hmac(signData));
		String token=Base64.encodeBase64URLSafeString(signData.getBytes())+"."+sign;
		return token;
	}
	public  String createToken(String hmacKey,Map<String,String> data) {
		String signData=mapToString(data);
		String sign = Base64.encodeBase64URLSafeString(new HmacUtils(HmacAlgorithms.HMAC_MD5, hmacKey).hmac(signData));
		String token=Base64.encodeBase64URLSafeString(signData.getBytes())+"."+sign;
		return token;
	}
	
	public   Map<String,String>  vafy(String token) throws SecurityException  {
			if(null==token||"".equals(token)) {
				throw new SecurityException("token格式非法");
			}
			String[] split = token.split("\\.");
			if(split.length>2) {
				throw new SecurityException("token格式非法");
			}
			String sign=split[1];
			byte[] srcData=Base64.decodeBase64(split[0]);
			String data=new String(srcData);
			String sign2 = Base64.encodeBase64URLSafeString(hmac.hmac(srcData));
			if(!sign2.equals(sign)){
				throw new SecurityException("token签名非法");
			}
			Map<String, String> stringToMap = stringToMap(data);
			if(!stringToMap.containsKey("end")){
				return stringToMap;
			}
			Long end=Long.parseLong(stringToMap.get("end"));
			if(System.currentTimeMillis()>end){
				throw new SecurityException("token过期");
			}
			return stringToMap;
		
	}
	private Map<String,String> stringToMap(String mapString){
		String[] split = mapString.split(";");
		Map<String,String> map=new HashMap<String, String>(split.length);
		for(String keyValue:split){
			String[] kv = keyValue.split("=");
			map.put(kv[0], kv[1]);
		}
		return map;
	}
	private String mapToString(Map<String,String> map){
		StringBuilder buf=new StringBuilder();
		map.forEach((key,value)->{
			buf.append(key).append("=").append(map.get(key)).append(";");
		});
		return buf.substring(0, buf.length()-1);
		
	}

}
