package com.taoyuanx.ca.exception;

/**
 * @author 都市桃源 2018年5月25日下午11:03:46
 * token 异常
 */
public class TokenException extends RuntimeException {

    private static final long serialVersionUID = -4643844376114736662L;
    public ExceptionType type;

    public TokenException(ExceptionType type, String exception) {
        super(type.msg + exception);
        this.type = type;
    }

}
