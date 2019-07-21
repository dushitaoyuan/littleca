package com.taoyuanx.ca.common;
/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    OK(1),//成功
    FAIL(0),//失败
    UNAUTHORIZED(401),//未认证
    NOT_FOUND(404),//接口不存在
    METHOD_NOT_SUPPORT(405),//http方法不支持
    PARAM_ERROR(400),//参数异常
    SERVICE_EX(101),//业务异常
    UN_SUPPORT_MEDIATYPE(415),//不支持媒体类型
    INTERNAL_SERVER_ERROR(500),//服务器内部错误
	SERVER_UNAVAILABLE(503);//服务端当前无法处理请求
    
    
    public int code;
 
    ResultCode(int code) {
        this.code = code;
    }
   }

