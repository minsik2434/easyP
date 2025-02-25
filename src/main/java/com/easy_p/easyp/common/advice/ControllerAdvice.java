package com.easy_p.easyp.common.advice;

import com.easy_p.easyp.common.exception.*;
import com.easy_p.easyp.dto.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(OAuth2Exception.class)
    public ResponseEntity<ExceptionResponse> authExceptionHandle(OAuth2Exception ex){
        ExceptionResponse response = buildExceptionResponse(ex.getStatusCode(), "OAuthException", ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(UnSupportedHandlerException.class)
    public ResponseEntity<ExceptionResponse> unSupportedHandlerExceptionHandle(UnSupportedHandlerException ex){
        ExceptionResponse response =
                buildExceptionResponse(HttpServletResponse.SC_BAD_REQUEST, "UnSupported", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestExceptionHandler(BadRequestException ex){
        ExceptionResponse response =
                buildExceptionResponse(HttpServletResponse.SC_BAD_REQUEST, "UnSupported", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ExceptionResponse> refreshExceptionHandler(JwtTokenException ex){
        ExceptionResponse response =
                buildExceptionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundExceptionHandler(NotFoundException ex){
        ExceptionResponse response =
                buildExceptionResponse(HttpServletResponse.SC_NOT_FOUND, "Not Found", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    public ExceptionResponse buildExceptionResponse(int status, String error, String message){
        return ExceptionResponse.builder()
                .status(status)
                .error(error)
                .message(message).build();
    }
}
