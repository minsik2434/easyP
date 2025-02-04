package com.easy_p.easyp.controller;

import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.dto.Auth2Login;
import com.easy_p.easyp.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/oauth2/{authType}/login")
    public ResponseEntity<JwtToken> oauth2Authenticate(@PathVariable("authType") String authType, @RequestBody Auth2Login auth2Login,
                                                       HttpServletResponse response) {

        JwtToken jwtToken = memberService.processOAuth2Login(authType, auth2Login.getCode());
        String refreshToken = jwtToken.getRefreshToken();
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // HTTPS에서만 전송되도록 설정
                .sameSite("None") // SameSite 속성 설정
                .path("/") // 전체 경로에 대해 쿠키가 사용될 수 있도록 설정
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
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
    public String test(HttpServletRequest request){
//        log.info("Cookie: {}", token);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                // refreshToken 쿠키를 찾으면 그 값을 사용
                String refreshToken = cookie.getValue();
                // refreshToken을 검증하거나 사용
                log.info("cookie value = {}", refreshToken);
            }
        }
        return "로그인 성공!!";
    }
}
