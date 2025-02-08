package com.easy_p.easyp.service.member;

import com.easy_p.easyp.common.exception.JwtTokenException;
import com.easy_p.easyp.common.exception.NotFoundException;
import com.easy_p.easyp.common.jwt.JwtProvider;
import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.common.jwt.RefreshTokenStore;
import com.easy_p.easyp.common.oauth2.OAuthManager;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.dto.MemberContext;
import com.easy_p.easyp.dto.UserAuthDto;
import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.repository.MemberRepository;
import com.easy_p.easyp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final OAuthManager oAuthManager;
    private final RefreshTokenStore refreshTokenStore;

    @Transactional
    @Override
    public UserAuthDto processOAuth2Login(String authType, String authCode) {
        JwtToken jwtToken;
        UserAuthDto userAuthDto;
        UserInfo userInfo = oAuthManager.getUserInfo(authType, authCode);
        String email = userInfo.getEmail();
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if(optionalMember.isPresent()) {
            jwtToken = jwtProvider.createToken(email);
            userAuthDto = buildUserAuthDto(optionalMember.get(),jwtToken);
        }
        else{
            Member member = new Member(email, userInfo.getName(), "MEMBER", userInfo.getProfile());
            Member save = memberRepository.save(member);
            jwtToken = jwtProvider.createToken(save.getEmail());
            userAuthDto = buildUserAuthDto(save, jwtToken);
        }
        refreshTokenStore.storeRefreshToken(email, jwtToken.getRefreshToken());
        return userAuthDto;
    }

    @Override
    public String generateOAuth2AuthorizationUrl(String authType) {
        return oAuthManager.getAuthRequestUri(authType);
    }

    @Override
    public JwtToken processTokenRefresh(String refreshToken) {

        jwtProvider.validateToken(refreshToken);

        String email = jwtProvider.getClaim(refreshToken, "email");
        String savedToken = refreshTokenStore.getRefreshToken(email);
        if(savedToken == null || !savedToken.equals(refreshToken)){
            //TODO 토큰 Invalid 예외 처리 ControllerAdvice 에 구현해야함
            throw new JwtTokenException("Invalid Refresh Token");
        }
        JwtToken token = jwtProvider.createToken(email);
        refreshTokenStore.storeRefreshToken(email, token.getRefreshToken());
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> optional = memberRepository.findByEmail(email);
        if(optional.isEmpty()){
            throw new NotFoundException("Not Found Member");
        }
        Member member = optional.get();
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(optional.get().getRole()));
        return new MemberContext(member, authorities);
    }

    private UserAuthDto buildUserAuthDto(Member member, JwtToken jwtToken){
        return new UserAuthDto(
                member.getEmail(),
                jwtToken,
                member.getRole(),
                member.getName(),
                member.getProfile()
        );
    }
}
