package com.taoyuanx.ca.encode.api;


import com.taoyuanx.ca.exception.EncodeException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * 对称加密配置
 * 
 * @author 都市桃源 2018年6月2日下午8:23:18 TODO type 加密长度 len 加密块长度 iv
 * 
 *  初始向量 CBC和CFB需要
 *  password 对称加密密码 
 *  这里不介绍 PBE 只使用 用户口令作为密码 可自行实现
 */
public class SymmetricConfig {
	/**type 加密类型
	 * iv 初始向量
	 * password 对称加密密码
	 * key 对称key 用户可以自定义,也可根据密码生成
	 * needBC 是否使用BC
	 */
	private String type;
	private byte[] iv;
	private String password;
	private boolean hasIv;
	private Key key;
	private boolean neddBC=false;
	public boolean isNeddBC() {
		return neddBC;
	}

	public void neddBC() {
		this.neddBC = true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	/** 
	 * 判断是否需要 初始向量CBC和 CFB 填充模式需要
	 * @return
	 */
	public boolean hasIv() {
		if(hasIv) {
			return hasIv;
		}
		hasIv=type.toUpperCase().indexOf("CBC") > 0 || type.toUpperCase().indexOf("CFB") > 0;
		return hasIv;
	}

	public Key getKey() {
		if(null==key) {
			int index = type.indexOf("/");
			if(index<0) {
				throw new EncodeException("请指定加密模式和填充模式");
			}
			key= new SecretKeySpec(password.getBytes(),type.substring(0,index));
		}
		return  key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	
	
	
	
}