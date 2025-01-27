package com.easy_p.easyp.common.exception;

public class JwtTokenException extends RuntimeException{
    public JwtTokenException(String message) {
        super(message);
    }
}
