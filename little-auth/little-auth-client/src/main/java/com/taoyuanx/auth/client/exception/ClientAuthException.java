package com.taoyuanx.auth.client.exception;



/**
 * 认证异常
 */
public class ClientAuthException extends RuntimeException {
    private static final long serialVersionUID = 8793672380339632040L;
    private Integer errorCode;

    public ClientAuthException(String msg) {
        super(msg);
    }

    public ClientAuthException(Integer errorCode, String msg) {
        super(msg);
        if (errorCode != null) {
            this.errorCode = errorCode;
        }

    }
    public Integer getErrorCode() {
        return errorCode;
    }

    public ClientAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
