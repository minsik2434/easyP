package com.easy_p.easyp.common.exception;

import lombok.Getter;

@Getter
public class OAuth2Exception extends RuntimeException{
    private final int statusCode;
    public OAuth2Exception(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public OAuth2Exception(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
