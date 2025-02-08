package com.easy_p.easyp.service;

import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.dto.UserAuthDto;

public interface MemberService {
    UserAuthDto processOAuth2Login(String authType, String authCode);
    String generateOAuth2AuthorizationUrl(String authType);
    JwtToken processTokenRefresh(String refreshToken);
}
