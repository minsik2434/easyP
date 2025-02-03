package com.easy_p.easyp.common.oauth2;

import com.easy_p.easyp.common.exception.UnSupportedHandlerException;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.common.oauth2.provider.OAuth2Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthManager {
    // OAuth2Provider 인터페이스를 구현한 빈 조회
    private final List<OAuth2Handler> oAuth2Handlers;
    public UserInfo getUserInfo(String authType, String authCode){
        return oAuth2Handlers.stream()
                .filter(oAuth2Handler -> oAuth2Handler.supports(authType))
                .findFirst()
                .orElseThrow(() -> new UnSupportedHandlerException("UnSupported Type: "+authType))
                .getUserInfo(authCode);
    }
    public String getAuthRequestUri(String authType){
        return oAuth2Handlers.stream()
                .filter(oAuth2Handler -> oAuth2Handler.supports(authType))
                .findFirst()
                .orElseThrow(() -> new UnSupportedHandlerException("UnSupported Type: "+authType))
                .getAuthRequestUrl();
    }
}
