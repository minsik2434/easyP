package com.easy_p.easyp.common.oauth2.provider;

import com.easy_p.easyp.common.oauth2.dto.GoogleUserInfo;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GoogleOAuth2Provider extends AbstractOAuth2Provider{

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;

    public GoogleOAuth2Provider(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String fetchOAuth2AccessToken(String code, String redirectUrl) {
        HttpEntity<MultiValueMap<String, String>> accessTokenEntity =
                buildFetchAccessTokenEntity(code, redirectUrl);
        ResponseEntity<String> response =
                sendRequest("https://oauth2.googleapis.com/token", HttpMethod.POST, accessTokenEntity);
        JsonNode jsonNode = parseJson(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    @Override
    protected UserInfo fetchOAuth2UserInfo(String accessToken) {
        HttpEntity<String> userInfoEntity = buildFetchUserInfoEntity(accessToken);
        ResponseEntity<String> response =
                sendRequest("https://www.googleapis.com/oauth2/v3/userinfo", HttpMethod.GET, userInfoEntity);
        JsonNode jsonNode = parseJson(response.getBody());
        return new GoogleUserInfo(
                jsonNode.get("sub").asText(),
                jsonNode.get("email").asText(),
                jsonNode.get("name").asText(),
                jsonNode.get("picture").asText()
        );
    }

    @Override
    public boolean supports(String type) {
        return type.equals("google");
    }

    private HttpEntity<MultiValueMap<String, String>> buildFetchAccessTokenEntity(String code, String redirectUrl){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUrl);
        body.add("grant_type", "authorization_code");
        return new HttpEntity<MultiValueMap<String, String>>(body, headers);
    }

    private HttpEntity<String> buildFetchUserInfoEntity(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return new HttpEntity<>(headers);
    }
}
