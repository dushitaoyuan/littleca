package com.taoyuanx.ca.ex;

import com.taoyuanx.ca.anno.CustomExceptionCode;

/**
 * @author 都市桃源
 * 参数校验异常
 */
@CustomExceptionCode(code=400,msg="参数异常")
public class ValidatorException extends CustomException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2887718118671856033L;

	public ValidatorException() {
		super();
	}

	public ValidatorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ValidatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidatorException(String message) {
		super(message);
	}

	
	
}