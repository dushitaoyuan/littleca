package com.taoyuanx.ca.exception;

/**
 * @author 都市桃源
 * 2018年5月30日下午10:13:31
 *  加解密异常
 */
public class EncodeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5077797844780947788L;

	public EncodeException(String message,Exception e) {
		super(message,e);
	}
	public EncodeException(String message) {
		super(message);
	}
	

}
