package com.easy_p.easyp.common.exception;

public class OAuth2Exception extends RuntimeException{
    public OAuth2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuth2Exception(String message) {
        super(message);
    }
}
