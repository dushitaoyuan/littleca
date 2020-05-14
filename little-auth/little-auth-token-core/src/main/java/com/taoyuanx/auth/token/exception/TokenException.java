package com.taoyuanx.auth.token.exception;



/**
 * 认证异常
 */
public class TokenException extends RuntimeException {
    private static final long serialVersionUID = 8793672380339632040L;
    private Integer errorCode;

    public TokenException(String msg) {
        super(msg);
    }

    public TokenException(Integer errorCode, String msg) {
        super(msg);
        if (errorCode != null) {
            this.errorCode = errorCode;
        }

    }

    public Integer getErrorCode() {
        return errorCode;
    }


}
