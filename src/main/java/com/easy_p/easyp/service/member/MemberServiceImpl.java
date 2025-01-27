package com.easy_p.easyp.service.member;

import com.easy_p.easyp.common.exception.NotFoundException;
import com.easy_p.easyp.common.jwt.JwtProvider;
import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.common.oauth2.OAuth2AuthenticationManager;
import com.easy_p.easyp.common.oauth2.dto.GoogleUserInfo;
import com.easy_p.easyp.dto.Auth2Login;
import com.easy_p.easyp.dto.MemberContext;
import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.repository.MemberRepository;
import com.easy_p.easyp.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;
    private final MemberRepository memberRepository;
    ObjectMapper mapper = new ObjectMapper();
    private final JwtProvider jwtProvider;
    private final OAuth2AuthenticationManager oAuth2AuthenticationManager;

    @Transactional
    @Override
    public JwtToken oauth2Login(Auth2Login auth2Login) {
        JwtToken jwtToken;
        GoogleUserInfo userInfo = oAuth2AuthenticationManager.getUserInfo(auth2Login);
        String email = userInfo.getEmail();
        Optional<Member> optional = memberRepository.findByEmail(email);
        if(optional.isPresent()) {
            jwtToken = jwtProvider.createToken(email);
        }
        else{
            Member member = new Member(email, userInfo.getName(), "MEMBER", userInfo.getPicture());
            Member save = memberRepository.save(member);
            jwtToken = jwtProvider.createToken(save.getEmail());
        }
        return jwtToken;
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
