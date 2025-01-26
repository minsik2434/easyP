package com.easy_p.easyp.common.exception;

public class GoogleOAuth2Exception extends RuntimeException{
    public GoogleOAuth2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public GoogleOAuth2Exception(String message) {
        super(message);
    }
}
