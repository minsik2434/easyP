package com.easy_p.easyp.common.filter;

import com.easy_p.easyp.common.exception.JwtTokenException;
import com.easy_p.easyp.dto.response.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch(JwtTokenException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ExceptionResponse exceptionResponse = buildResponse(e);
            getMapper().writeValue(response.getOutputStream(), exceptionResponse);
        }
    }

    private ExceptionResponse buildResponse(JwtTokenException e){
        return ExceptionResponse.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error("Token Invalid")
                .message(e.getMessage())
                .build();
    }
    private ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
