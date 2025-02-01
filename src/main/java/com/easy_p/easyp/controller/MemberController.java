package com.easy_p.easyp.controller;

import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.dto.Auth2Login;
import com.easy_p.easyp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/oauth2/{authType}/login")
    public ResponseEntity<JwtToken> auth2Login(@PathVariable("authType") String authType, @RequestBody Auth2Login auth2Login) {
        JwtToken jwtToken = memberService.oauth2Login(authType, auth2Login.getCode());
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/oauth2/{authType}/requestUri")
    public ResponseEntity<String> oauth2RequestUri(@PathVariable("authType") String authType){
        String returnValue = memberService.oauthRequestUri(authType);
        return ResponseEntity.ok(returnValue);
    }
}
