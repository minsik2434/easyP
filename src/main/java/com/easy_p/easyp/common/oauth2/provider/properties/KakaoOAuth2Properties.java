package com.easy_p.easyp.common.oauth2.provider.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "oauth2.kakao")
public class KakaoOAuth2Properties {
    private String clientId;
    private String clientSecret;
    private String authorizationGrantType;
    private String accessTokenUri;
    private String userInfoUri;
    private String responseType;
    private String authorizationUri;
}
