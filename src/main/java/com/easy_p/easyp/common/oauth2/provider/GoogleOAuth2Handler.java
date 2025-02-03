package com.easy_p.easyp.common.oauth2.provider;

import com.easy_p.easyp.common.oauth2.dto.GoogleUserInfo;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.common.oauth2.provider.properties.GoogleOAuth2Properties;
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
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class GoogleOAuth2Handler extends AbstractOAuth2Handler {

    private final GoogleOAuth2Properties properties;
    @Value("${oauth2.redirect-uri}")
    private String redirectUrl;

    public GoogleOAuth2Handler(RestTemplate restTemplate, GoogleOAuth2Properties properties) {
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
        return new GoogleUserInfo(
                jsonNode.get("sub").asText(),
                jsonNode.get("email").asText(),
                jsonNode.get("name").asText(),
                jsonNode.get("picture").asText()
        );
    }

    @Override
    public String getAuthRequestUrl() {
        String scopeParam = String.join("+", properties.getScope());
        return UriComponentsBuilder.fromUriString(properties.getAuthorizationUri())
                .queryParam("client_id", properties.getClientId())
                .queryParam("response_type", properties.getResponseType())
                .queryParam("scope", scopeParam)
                .queryParam("redirect_uri", redirectUrl)
                .queryParam("state", "google").build().toUriString();
    }

    @Override
    public boolean supports(String type) {
        return type.equals("google");
    }

    private HttpEntity<MultiValueMap<String, String>> buildFetchAccessTokenEntity(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", properties.getClientId());
        body.add("client_secret", properties.getClientSecret());
        body.add("redirect_uri", redirectUrl);
        body.add("grant_type", properties.getAuthorizationGrantType());
        return new HttpEntity<MultiValueMap<String, String>>(body, headers);
    }

    private HttpEntity<String> buildFetchUserInfoEntity(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return new HttpEntity<>(headers);
    }
}
