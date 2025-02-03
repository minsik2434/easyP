package com.easy_p.easyp.controller;

import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.dto.Auth2Login;
import com.easy_p.easyp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/oauth2/{authType}/login")
    public ResponseEntity<JwtToken> oauth2Authenticate(@PathVariable("authType") String authType, @RequestBody Auth2Login auth2Login) {
        JwtToken jwtToken = memberService.processOAuth2Login(authType, auth2Login.getCode());
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/oauth2/{authType}/requestUri")
    public ResponseEntity<String> getOAuth2AuthorizationUrl(@PathVariable("authType") String authType){
        String returnValue = memberService.generateOAuth2AuthorizationUrl(authType);
        return ResponseEntity.ok(returnValue);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtToken> tokenRefresh(@RequestHeader("Authorization") String refreshToken){
        String token = refreshToken.replace("Bearer ", "");
        JwtToken jwtToken = memberService.processTokenRefresh(token);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
