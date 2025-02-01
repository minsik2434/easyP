package com.easy_p.easyp.common.oauth2.provider;

import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.dto.Auth2Login;

public interface OAuth2Provider {
    String getAuthRequestUrl();
    UserInfo getUserInfo(String code);
    boolean supports(String type);
}
