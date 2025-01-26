package com.easy_p.easyp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auth2Login {
    private String code;
    private String type;
    private String redirectUrl;
}
