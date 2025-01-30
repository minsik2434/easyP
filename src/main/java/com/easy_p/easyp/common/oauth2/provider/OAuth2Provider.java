package com.easy_p.easyp.common.oauth2.provider;

import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.dto.Auth2Login;

public interface OAuth2Provider {
    UserInfo getUserInfo(Auth2Login auth2Login);
    boolean supports(String type);
}
