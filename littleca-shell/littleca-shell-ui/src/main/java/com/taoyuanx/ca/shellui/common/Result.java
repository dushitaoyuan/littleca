package com.taoyuanx.ca.shellui.common;

public class Result<T>{
	private Integer status;//0失败1成功
	private String msg;//消息
	private Integer code;//消息码
	private T info;//结果实体
	private Object ext;// 全局，额外字段
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public T getInfo() {
		return info;
	}
	public void setInfo(T info) {
		this.info = info;
	}
	public Object getExt() {
		return ext;
	}
	public void setExt(Object ext) {
		this.ext = ext;
	}
 

}
