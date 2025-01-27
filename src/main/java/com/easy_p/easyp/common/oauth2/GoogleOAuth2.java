package com.easy_p.easyp.common.oauth2;

import com.easy_p.easyp.common.exception.GoogleOAuth2Exception;
import com.easy_p.easyp.common.oauth2.dto.GoogleUserInfo;
import com.easy_p.easyp.dto.Auth2Login;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2 {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;
    ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate;

    public GoogleUserInfo getUserInfo(Auth2Login auth2Login) {
        String code =URLDecoder.decode(auth2Login.getCode(), StandardCharsets.UTF_8);
        String redirectUrl = auth2Login.getRedirectUrl();
        String googleAccessToken = getGoogleAccessToken(code, redirectUrl);
        return getGoogleUserInfo(googleAccessToken);
    }

    private String getGoogleAccessToken(String code, String redirectUrl) {
        HttpEntity<MultiValueMap<String, String>> googleAuthRequest = createGoogleAuthRequest(code, redirectUrl);
        ResponseEntity<String> authResponse = postGoogleOAuthToken(googleAuthRequest);
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(authResponse.getBody());
        } catch (JsonProcessingException e) {
            log.info("googleAccessToken JsonProcessingException");
            throw new RuntimeException(e);
        }
        return jsonNode.get("access_token").asText();
    }

    private GoogleUserInfo getGoogleUserInfo(String accessToken) {
        HttpEntity<String> googleUserInfoRequest = createGoogleUserInfoRequest(accessToken);
        ResponseEntity<String> googleOAuthToken = getGoogleOAuthToken(googleUserInfoRequest);
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(googleOAuthToken.getBody());
        } catch (JsonProcessingException e) {
            log.info("googleUserInfo JsonProcessingException");
            throw new RuntimeException(e);
        }
        String sub = jsonNode.get("sub").asText();
        String email = jsonNode.get("email").asText();
        String name = jsonNode.get("name").asText();
        String picture = jsonNode.get("picture").asText();
        return new GoogleUserInfo(sub, email, name, picture);
    }

    private ResponseEntity<String> postGoogleOAuthToken(HttpEntity<MultiValueMap<String,String>> googleAuthRequest){
        log.info("auth : {}", googleAuthRequest.getBody().get("grant_type"));
        ResponseEntity<String> authResponse;
        try{
            authResponse = restTemplate.exchange("https://oauth2.googleapis.com/token", HttpMethod.POST,
                    googleAuthRequest, String.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new GoogleOAuth2Exception("Google AuthCode Authentication Exception: BadRequest", e);
            }
            else if(e.getStatusCode() == HttpStatus.UNAUTHORIZED){
                throw new GoogleOAuth2Exception("Google AuthCode Authentication Exception: UnAuthorized", e);
            }
            else if(e.getStatusCode() == HttpStatus.FORBIDDEN){
                throw new GoogleOAuth2Exception("Google AuthCode Authentication Exception: Forbidden", e);
            }
            else{
                throw new GoogleOAuth2Exception("Google AuthCode Authentication Exception: ServerError", e);
            }
        }
        return authResponse;
    }

    private ResponseEntity<String> getGoogleOAuthToken(HttpEntity<String> googleAuthRequest){
        ResponseEntity<String> authResponse;
        try{
            authResponse = restTemplate.exchange("https://www.googleapis.com/oauth2/v3/userinfo", HttpMethod.GET,
                    googleAuthRequest, String.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new GoogleOAuth2Exception("Google AuthCode Authentication Exception: BadRequest", e);
            }
            else if(e.getStatusCode() == HttpStatus.UNAUTHORIZED){
                throw new GoogleOAuth2Exception("Google AuthCode Authentication Exception: UnAuthorized", e);
            }
            else if(e.getStatusCode() == HttpStatus.FORBIDDEN){
                throw new GoogleOAuth2Exception("Google AuthCode Authentication Exception: Forbidden", e);
            }
            else{
                throw new GoogleOAuth2Exception("Google AuthCode Authentication Exception: ServerError", e);
            }
        }
        return authResponse;
    }

    private HttpEntity<MultiValueMap<String, String>> createGoogleAuthRequest(String code, String redirectUrl){
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

    private HttpEntity<String> createGoogleUserInfoRequest(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return new HttpEntity<>(headers);
    }
}
