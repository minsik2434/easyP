package com.easy_p.easyp.common.oauth2;

import com.easy_p.easyp.common.oauth2.dto.GoogleUserInfo;
import com.easy_p.easyp.dto.Auth2Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationManager {
    private final GoogleOAuth2 googleOAuth2;
    public GoogleUserInfo getUserInfo(Auth2Login auth2Login){
        return googleOAuth2.getUserInfo(auth2Login);
    }
}
