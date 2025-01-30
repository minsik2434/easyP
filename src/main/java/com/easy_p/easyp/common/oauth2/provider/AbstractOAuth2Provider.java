package com.easy_p.easyp.common.oauth2.provider;

import com.easy_p.easyp.common.exception.JsonParsingException;
import com.easy_p.easyp.common.exception.OAuth2Exception;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.dto.Auth2Login;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public final UserInfo getUserInfo(Auth2Login auth2Login){
        String code = URLDecoder.decode(auth2Login.getCode(), StandardCharsets.UTF_8);
        String redirectUrl = auth2Login.getRedirectUrl();

        String accessToken = fetchOAuth2AccessToken(code, redirectUrl);
        return fetchOAuth2UserInfo(accessToken);
    }

    protected abstract String fetchOAuth2AccessToken(String code, String redirectUrl);
    protected abstract UserInfo fetchOAuth2UserInfo(String accessToken);

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
