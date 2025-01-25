package com.easy_p.easyp.service.member;

import com.easy_p.easyp.dto.Auth2Login;
import com.easy_p.easyp.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public Auth2Login authentication(Auth2Login auth2Login) {
        String code = auth2Login.getCode();
        String redirectUrl = auth2Login.getRedirectUrl();
        RestTemplate restTemplate = new RestTemplate();
        String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        LinkedMultiValueMap<String, String> body =
                createGoogleAuthRequestBody(decodedCode, clientId, clientSecret, redirectUrl);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try{
            ResponseEntity<String> response =
                    restTemplate.exchange("https://oauth2.googleapis.com/token", HttpMethod.POST, entity, String.class);

            JsonNode jsonNode = mapper.readTree(response.getBody());
            String accessToken = jsonNode.get("access_token").asText();
            log.info("accessToekn :{}" , accessToken);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization","Bearer" + accessToken);
            HttpEntity<String> eps = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> exchange = restTemplate.exchange("https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET, eps, String.class);
            log.info("{}", exchange.getBody());
        } catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new RuntimeException(e);
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 응답 처리
        return null;
    }

    private LinkedMultiValueMap<String, String> createGoogleAuthRequestBody(String code, String clientId, String clientSecret,
                                                                            String redirectUri){
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");
        return body;
    }
}
