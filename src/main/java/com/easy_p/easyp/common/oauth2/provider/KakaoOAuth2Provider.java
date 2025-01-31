package com.easy_p.easyp.common.oauth2.provider;

import com.easy_p.easyp.common.oauth2.dto.KakaoUserInfo;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
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

@Component
public class KakaoOAuth2Provider extends AbstractOAuth2Provider{
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    String clientSecret;
    public KakaoOAuth2Provider(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String fetchOAuth2AccessToken(String code, String redirectUrl) {
        HttpEntity<MultiValueMap<String, String>> accessTokenEntity =
                buildFetchAccessTokenEntity(code, redirectUrl);
        ResponseEntity<String> response =
                sendRequest("https://kauth.kakao.com/oauth/token", HttpMethod.POST, accessTokenEntity);
        JsonNode jsonNode = parseJson(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    @Override
    protected UserInfo fetchOAuth2UserInfo(String accessToken) {
        HttpEntity<String> userInfoEntity = buildFetchUserInfoEntity(accessToken);
        ResponseEntity<String> response =
                sendRequest("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, userInfoEntity);
        JsonNode jsonNode = parseJson(response.getBody());
        String id = jsonNode.get("id").asText();
        JsonNode account = jsonNode.get("kakao_account");
        String nickname = account.get("profile").get("nickname").asText();
        String profileImage = account.get("profile").get("profile_image_url").asText();
        String email = account.get("email").asText();
        return new KakaoUserInfo(id, email, nickname, profileImage);
    }

    @Override
    public boolean supports(String type) {
        return type.equals("kakao");
    }

    private HttpEntity<MultiValueMap<String, String>> buildFetchAccessTokenEntity(String code, String redirectUrl){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUrl);
        body.add("code", code);
        body.add("client_secret", clientSecret);
        return new HttpEntity<MultiValueMap<String, String>>(body, headers);
    }

    private HttpEntity<String> buildFetchUserInfoEntity(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return new HttpEntity<>(headers);
    }
}