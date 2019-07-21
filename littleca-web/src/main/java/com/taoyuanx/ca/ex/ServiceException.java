package com.taoyuanx.ca.ex;

import com.taoyuanx.ca.anno.CustomExceptionCode;


/**
 * @author 都市桃源
 * 所有的业务异常必须继承该异常
 * 返回 http状态码 200 
 */
@CustomExceptionCode(code=101,msg="业务异常")
public class ServiceException extends CustomException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -651804261815318584L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}





	
	

}
