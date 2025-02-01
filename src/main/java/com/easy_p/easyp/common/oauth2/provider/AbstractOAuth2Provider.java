package com.easy_p.easyp.common.oauth2.provider;

import com.easy_p.easyp.common.exception.JsonParsingException;
import com.easy_p.easyp.common.exception.OAuth2Exception;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.dto.Auth2Login;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public abstract class AbstractOAuth2Provider implements OAuth2Provider{
    protected final RestTemplate restTemplate;
    protected final ObjectMapper mapper = new ObjectMapper();

    public AbstractOAuth2Provider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    /*
    템플릿 메서드 패턴 적용
    유저 정보 조회 알고리즘 정의 후 fetchOAuth2AccessToken , fetchOAuth2UserInfo 메서드 하위 클래스에서 구현
    1. 인증 서버에서 AuthCode 와 Redirect URL 을 전달해 AccessToken 을 발급
    2. 발급받는 AccessToken 으로 유저 정보 서버에서 조회
     */
    public final UserInfo getUserInfo(String authCode){
        String code = URLDecoder.decode(authCode, StandardCharsets.UTF_8);
        String accessToken = fetchOAuth2AccessToken(code);
        return fetchOAuth2UserInfo(accessToken);
    }
    /**
     * Auth Code 로 AccessToken 받아오는 추상 메서드
     * @param code AuthCode
     * @return AccessToken(String)
     */
    protected abstract String fetchOAuth2AccessToken(String code);

    /**
     * 발급받은 AccessToken 으로 유저 정보 조회
     * @param accessToken 발급받은 AccessToken
     * @return UserInfo 인터페이스 -> 유저 정보
     */
    protected abstract UserInfo fetchOAuth2UserInfo(String accessToken);

    /**
     * RestTemplate 요청
     * @param url 요청 URL
     * @param method 메서드
     * @param requestEntity 요청 바디
     * @return ResponseEntity JSON 응답
     */
    protected ResponseEntity<String> sendRequest(String url, HttpMethod method, HttpEntity<?> requestEntity){
        try{
            return restTemplate.exchange(url, method, requestEntity, String.class);
        } catch(HttpClientErrorException e){
            throw handleOAuth2Exception(e);
        }
    }

    protected JsonNode parseJson(String responseBody){
        try{
            return mapper.readTree(responseBody);
        } catch (JsonProcessingException e){
            throw new JsonParsingException("JsonParseException", e);
        }
    }

    private OAuth2Exception handleOAuth2Exception(HttpClientErrorException e){
        int value = e.getStatusCode().value();
        String message = switch (value){
            case 400 -> "OAuth2Exception: BadRequest";
            case 401 -> "OAuth2Exception: Unauthorized";
            case 403 -> "OAuth2Exception: Forbidden";
            default -> "OAuth2Exception: ServerError";
        };
        return new OAuth2Exception(message, e);
    }
}
