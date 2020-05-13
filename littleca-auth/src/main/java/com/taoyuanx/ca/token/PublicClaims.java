package com.taoyuanx.ca.token;

/**
 * @author 都市桃源
 * 2018年5月27日上午12:32:26
 * 非敏感参数key值
 */
public interface PublicClaims  {
	/**
	 * 自定义公开信息
	 */
    String PUB = "pub";
    /**
     * token 类型标识
     */
    String FLG="flg";
    /**
     * 用户id 
     */
	String USERID="uid";
	 /**
     * 会话id
     */
	String SESSIONID="sid";
    
}
