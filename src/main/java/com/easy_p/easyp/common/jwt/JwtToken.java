package com.easy_p.easyp.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}
