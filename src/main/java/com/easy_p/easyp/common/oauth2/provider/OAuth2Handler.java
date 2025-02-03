package com.easy_p.easyp.common.oauth2.provider;

import com.easy_p.easyp.common.oauth2.dto.UserInfo;

public interface OAuth2Handler {
    String getAuthRequestUrl();
    UserInfo getUserInfo(String code);
    boolean supports(String type);
}
