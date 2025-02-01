package com.easy_p.easyp.common.oauth2.provider;

import com.easy_p.easyp.common.oauth2.dto.KakaoUserInfo;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.common.oauth2.provider.properties.KakaoOAuth2Properties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoOAuth2Provider extends AbstractOAuth2Provider{

    private final KakaoOAuth2Properties properties;
    @Value("${oauth2.redirect-uri}")
    private String redirectUri;
    public KakaoOAuth2Provider(RestTemplate restTemplate, KakaoOAuth2Properties properties) {
        super(restTemplate);
        this.properties = properties;
    }

    @Override
    protected String fetchOAuth2AccessToken(String code) {
        HttpEntity<MultiValueMap<String, String>> accessTokenEntity =
                buildFetchAccessTokenEntity(code);
        ResponseEntity<String> response =
                sendRequest(properties.getAccessTokenUri(), HttpMethod.POST, accessTokenEntity);
        JsonNode jsonNode = parseJson(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    @Override
    protected UserInfo fetchOAuth2UserInfo(String accessToken) {
        HttpEntity<String> userInfoEntity = buildFetchUserInfoEntity(accessToken);
        ResponseEntity<String> response =
                sendRequest(properties.getUserInfoUri(), HttpMethod.GET, userInfoEntity);
        JsonNode jsonNode = parseJson(response.getBody());
        String id = jsonNode.get("id").asText();
        JsonNode account = jsonNode.get("kakao_account");
        String nickname = account.get("profile").get("nickname").asText();
        String profileImage = account.get("profile").get("profile_image_url").asText();
        String email = account.get("email").asText();
        return new KakaoUserInfo(id, email, nickname, profileImage);
    }

    @Override
    public String getAuthRequestUrl() {
        return UriComponentsBuilder.fromUriString(properties.getAuthorizationUri())
                .queryParam("client_id", properties.getClientId())
                .queryParam("response_type", properties.getResponseType())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", "kakao").build().toUriString();
    }

    @Override
    public boolean supports(String type) {
        return type.equals("kakao");
    }

    private HttpEntity<MultiValueMap<String, String>> buildFetchAccessTokenEntity(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.getAuthorizationGrantType());
        body.add("client_id", properties.getClientId());
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", properties.getClientSecret());
        return new HttpEntity<MultiValueMap<String, String>>(body, headers);
    }

    private HttpEntity<String> buildFetchUserInfoEntity(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return new HttpEntity<>(headers);
    }
}