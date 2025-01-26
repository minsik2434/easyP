package com.easy_p.easyp.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    @Value("{jwt.secret}")
    private String keyString;
    @Value("{jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("{jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;
}
