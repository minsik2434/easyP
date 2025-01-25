package com.easy_p.easyp.service;

import com.easy_p.easyp.dto.Auth2Login;

public interface MemberService {
    Auth2Login authentication(Auth2Login auth2Login);
}
