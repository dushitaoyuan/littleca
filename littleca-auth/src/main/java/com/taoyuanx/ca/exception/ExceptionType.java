package com.taoyuanx.ca.exception;

/**
 * @author 都市桃源
 * 2018年5月25日下午11:10:12
 * token 异常类型
 */
public enum ExceptionType {
	EXPIRE("过期异常"),
	FORMAT("格式异常"),
	NOTBEFORE("未生效异常"),
	SIGN("签名异常");
	public String msg;
	private ExceptionType(String msg) {
		this.msg = msg;
	}
	

}
