package com.taoyuanx.ca.exception;

/**
 * @author 都市桃源
 * 2018年5月30日下午10:13:31
 *  签名异常
 */
public class SignException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5077797844780947788L;

	public SignException(String message,Exception e) {
		super(message,e);
	}
	public SignException(String message) {
		super(message);
	}
	

}
