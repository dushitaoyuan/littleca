package com.taoyuanx.auth.exception;



/**
 * 认证异常
 */
public class AuthException extends RuntimeException {
    private static final long serialVersionUID = 8793672380339632040L;
    private Integer errorCode;

    public AuthException(String msg) {
        super(msg);
    }

    public AuthException(Integer errorCode, String msg) {
        super(msg);
        if (errorCode != null) {
            this.errorCode = errorCode;
        }

    }

    public Integer getErrorCode() {
        return errorCode;
    }


}
