package com.taoyuanx.ca.web.ex;

/**
 * @author 都市桃源
 *自定义异常父类
 */
public class CustomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomException() {
		super();
	}

	public CustomException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomException(String message) {
		super(message);
	}

	public CustomException(Throwable cause) {
		super(cause);
	}
	

}
