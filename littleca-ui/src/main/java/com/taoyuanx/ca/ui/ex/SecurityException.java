package com.taoyuanx.ca.ui.ex;

import com.taoyuanx.ca.ui.anno.CustomExceptionCode;


/**
 * @author 都市桃源
 * 权限异常必须继承该异常
 * 返回 http状态码 200 
 */
@CustomExceptionCode(code=401,msg="无操作权限")
public class SecurityException extends CustomException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -651804261815318584L;

	public SecurityException() {
		super();
	}

	public SecurityException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityException(String message) {
		super(message);
	}





	
	

}
