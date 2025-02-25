package com.easy_p.easyp.controller;

import com.easy_p.easyp.common.exception.BadRequestException;
import com.easy_p.easyp.common.exception.PermissionException;
import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.dto.request.Auth2Login;
import com.easy_p.easyp.dto.MemberAuthDto;
import com.easy_p.easyp.dto.MemberContext;
import com.easy_p.easyp.dto.request.BookmarkDto;
import com.easy_p.easyp.dto.response.AuthResponse;
import com.easy_p.easyp.dto.response.MemberInfo;
import com.easy_p.easyp.dto.PageDto;
import com.easy_p.easyp.dto.response.RefreshResponse;
import com.easy_p.easyp.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/oauth2/{authType}/login")
    public ResponseEntity<AuthResponse> oauth2Authenticate(@PathVariable("authType") String authType, @RequestBody Auth2Login auth2Login,
                                                       HttpServletResponse response) {
        MemberAuthDto memberAuthDto = memberService.processOAuth2Login(authType, auth2Login.getCode());
        AuthResponse authResponse = buildAuthResponse(memberAuthDto);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", memberAuthDto.getJwtToken().getRefreshToken())
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

    @GetMapping("/{email}/info")
    public ResponseEntity<MemberInfo> memberInfo(@PathVariable("email") String email){
        MemberInfo memberInfo = memberService.getMemberInfo(email);
        return ResponseEntity.ok(memberInfo);
    }

    @GetMapping("/{email}/project")
    public ResponseEntity<PageDto> belongProject(@PathVariable("email") String email,
                                                 @PageableDefault(page = 0, size=10, sort="id"
                                                                       , direction = Sort.Direction.ASC) Pageable pageable,
                                                 @AuthenticationPrincipal MemberContext memberContext){
        List<String> allowSortColumn = Arrays.asList("createAt", "updateAt", "id", "name");

        for(Sort.Order order : pageable.getSort()){
            if(!allowSortColumn.contains(order.getProperty())){
                throw new BadRequestException(order.getProperty() + " is a sorting column that is not supported");
            }
        }

        verifyingAccessRight(email, memberContext);
        PageDto belongProject = memberService.getBelongProject(email, pageable);
        return ResponseEntity.ok(belongProject);
    }

    @PostMapping("/bookmark/{projectId}")
    public ResponseEntity<Void> bookmarking(@PathVariable("projectId") Long projectId,
                                         @AuthenticationPrincipal MemberContext memberContext){
        String email = memberContext.getUsername();
        memberService.saveBookmark(email, projectId);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).build();
    }

    @GetMapping("/bookmark/{email}")
    public ResponseEntity<PageDto> getBookmarkingProject(@PathVariable("email") String email,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                                         @AuthenticationPrincipal MemberContext memberContext){
        verifyingAccessRight(email,memberContext);
        Pageable pageable = PageRequest.of(page, size);
        PageDto bookmarkingProject = memberService.getBookmarkingProject(email, pageable);
        return ResponseEntity.ok(bookmarkingProject);
    }

    @PatchMapping("/bookmark/sequence/update")
    public ResponseEntity<Void> bookmarkUpdate(@RequestBody BookmarkDto bookmarkDto,
                                            @AuthenticationPrincipal MemberContext memberContext){
        memberService.updateBookmarkSequence(bookmarkDto.getBookmarkId(),
                bookmarkDto.getChangeSequence(),
                memberContext.getUsername());

        return ResponseEntity.ok().build();
    }

    private AuthResponse buildAuthResponse(MemberAuthDto memberAuthDto){
        return new AuthResponse(
                memberAuthDto.getEmail(),
                memberAuthDto.getName(),
                memberAuthDto.getProfile(),
                memberAuthDto.getRole(),
                memberAuthDto.getJwtToken().getAccessToken()
        );
    }
    private RefreshResponse buildRefreshResponse(String accessToken){
        return new RefreshResponse(accessToken);
    }

    private void verifyingAccessRight(String email, MemberContext memberContext){
        String username = memberContext.getUsername();
        if(!username.equals(email)){
            boolean isManager = memberContext.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch("MANAGER"::equals);
            if(!isManager){
                throw new PermissionException("Permission Denied");
            }
        }
    }
}
