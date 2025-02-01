package com.easy_p.easyp.service.member;

import com.easy_p.easyp.common.exception.NotFoundException;
import com.easy_p.easyp.common.jwt.JwtProvider;
import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.common.oauth2.OAuth2UserInfoManager;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.dto.Auth2Login;
import com.easy_p.easyp.dto.MemberContext;
import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.repository.MemberRepository;
import com.easy_p.easyp.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final OAuth2UserInfoManager oAuth2UserInfoProvider;

    @Transactional
    @Override
    public JwtToken oauth2Login(String authType, String authCode) {
        JwtToken jwtToken;
        UserInfo userInfo = oAuth2UserInfoProvider.getUserInfo(authType, authCode);
        String email = userInfo.getEmail();
        Optional<Member> optional = memberRepository.findByEmail(email);
        if(optional.isPresent()) {
            jwtToken = jwtProvider.createToken(email);
        }
        else{
            Member member = new Member(email, userInfo.getName(), "MEMBER", userInfo.getProfile());
            Member save = memberRepository.save(member);
            jwtToken = jwtProvider.createToken(save.getEmail());
        }
        return jwtToken;
    }

    @Override
    public String oauthRequestUri(String authType) {
        return oAuth2UserInfoProvider.getAuthRequestUri(authType);
    }

    @Override
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
}
