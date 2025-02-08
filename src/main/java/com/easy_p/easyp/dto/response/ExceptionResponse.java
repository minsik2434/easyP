package com.easy_p.easyp.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ExceptionResponse {
    private int status;
    private String error;
    private String message;
}
