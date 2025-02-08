package com.easy_p.easyp.controller;

import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.dto.Auth2Login;
import com.easy_p.easyp.dto.UserAuthDto;
import com.easy_p.easyp.dto.response.AuthResponse;
import com.easy_p.easyp.dto.response.RefreshResponse;
import com.easy_p.easyp.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/oauth2/{authType}/login")
    public ResponseEntity<AuthResponse> oauth2Authenticate(@PathVariable("authType") String authType, @RequestBody Auth2Login auth2Login,
                                                       HttpServletResponse response) {
        UserAuthDto userAuthDto = memberService.processOAuth2Login(authType, auth2Login.getCode());
        AuthResponse authResponse = buildAuthResponse(userAuthDto);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", userAuthDto.getJwtToken().getRefreshToken())
                .httpOnly(true)
                .path("/")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/oauth2/{authType}/requestUri")
    public ResponseEntity<String> getOAuth2AuthorizationUrl(@PathVariable("authType") String authType){
        String returnValue = memberService.generateOAuth2AuthorizationUrl(authType);
        return ResponseEntity.ok(returnValue);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> tokenRefresh(@CookieValue("refreshToken") String token, HttpServletResponse response){
        JwtToken jwtToken = memberService.processTokenRefresh(token);
        RefreshResponse refreshResponse = buildRefreshResponse(jwtToken.getAccessToken());
        ResponseCookie cookie = ResponseCookie.from("refreshToken", jwtToken.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(refreshResponse);
    }

    @GetMapping("/test")
    public String test(){
        return "로그인 성공!!";
    }

    private AuthResponse buildAuthResponse(UserAuthDto userAuthDto){
        return new AuthResponse(
                userAuthDto.getEmail(),
                userAuthDto.getName(),
                userAuthDto.getProfile(),
                userAuthDto.getRole(),
                userAuthDto.getJwtToken().getAccessToken()
        );
    }
    private RefreshResponse buildRefreshResponse(String accessToken){
        return new RefreshResponse(accessToken);
    }
}
