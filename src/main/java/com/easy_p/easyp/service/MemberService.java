package com.easy_p.easyp.service;

import com.easy_p.easyp.common.jwt.JwtToken;

public interface MemberService {
    JwtToken processOAuth2Login(String authType, String authCode);
    String generateOAuth2AuthorizationUrl(String authType);
    JwtToken processTokenRefresh(String refreshToken);
}
