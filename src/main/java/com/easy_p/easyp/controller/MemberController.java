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
    @PostMapping("/oauth2/login")
    public ResponseEntity<JwtToken> auth2Login(@RequestBody Auth2Login auth2Login){
        JwtToken authentication = memberService.oauth2Login(auth2Login);
        return ResponseEntity.ok(authentication);
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
