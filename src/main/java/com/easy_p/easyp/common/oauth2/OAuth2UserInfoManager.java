package com.easy_p.easyp.common.oauth2;

import com.easy_p.easyp.common.exception.OAuth2Exception;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.common.oauth2.provider.OAuth2Provider;
import com.easy_p.easyp.dto.Auth2Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2UserInfoManager {
    // OAuth2Provider 인터페이스를 구현한 빈 조회
    private final List<OAuth2Provider> oAuth2Providers;
    public UserInfo getUserInfo(Auth2Login auth2Login){
        String type = auth2Login.getType();
        return oAuth2Providers.stream()
                .filter(oAuth2Provider -> oAuth2Provider.supports(type))
                .findFirst()
                .orElseThrow(() -> new OAuth2Exception("UnSupported OAuth2"))
                .getUserInfo(auth2Login);

    }
}
