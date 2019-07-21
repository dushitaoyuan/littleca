package com.taoyuanx.ca.ex;

/**
 * @author dushitaoyuan
 * 参数异常
 */
public class ParamException extends ServiceException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4103437727458639146L;
	public ParamException() {
		super();
	}

	public ParamException(String msg){
		super(msg);
	}
}
