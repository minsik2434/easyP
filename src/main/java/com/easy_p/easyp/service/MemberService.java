package com.easy_p.easyp.service;

import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.dto.Auth2Login;

public interface MemberService {
    JwtToken oauth2Login(Auth2Login auth2Login);
}
