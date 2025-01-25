package com.easy_p.easyp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth2Login {
    private String code;
    private String type;
    private String redirectUrl;
}
