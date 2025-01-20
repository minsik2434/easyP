package com.easy_p.easyp.service.member;

import com.easy_p.easyp.config.security.OAuth2Info;
import com.easy_p.easyp.config.security.PrincipalDetails;
import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        /*
            1. 사용자가 로그인 요청을 함
            2. 클라이언트 서버가 Authorization Server 에게 로그인 페이지를 요청해 사용자에게 제공
            3. 제공된 로그인 페이지로 사용자가 로그인을 성공하게 되면 Authorization Server 가 클라이언트 서버에게
                Authorization Code 를 전달함
            4. 클라이언트 서버는 전달받은 Authorization Code 로 Authorization Server 에게 접속 요청을 함
            5. Authorization Server 는 code 를 확인 후 클라이언트서버에게 AccessToken 을 전달함
            6. 클라이언트 서버에서 AccessToken 을 받아서 OAuth2UserRequest 객체를 생성함
         */
        // 이 로직은 생성된 OAuth2UserRequest 객체를 Resource Server 에게 전달해 사용자 정보를 받아오는 로직
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        /* userRequest 에는 로그인 요청을 위임한 Authorization Server 의 정보가 들어있음
         */
        // 로그인 요청을 위임한 Authorization Server 의 아이디(카카오, 구글)를 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        /*
           userNameAttributeName 은 Authorization Server 에 등록되어있는 사용자의 ID 이다
           load(userRequest).getAttributes() 에도 포함되어 있는 값이다

           {
              "sub": "1234567890",  // 사용자 고유 ID
              "name": "John Doe",   // 사용자 이름
              "given_name": "John", // 이름
              "family_name": "Doe", // 성
              "email": "johndoe@example.com", // 이메일
              "picture": "https://example.com/photo.jpg", // 프로필 사진
              "locale": "en" // 로케일 정보
            }
            그런데 userNameAttributeName 을 load(userRequest).getAttributes() 로 받아온 객체에서 뽑아내지 않는 이유는
            OAuth2 서버마다 이 고유 아이디 키 값이 다르기 때문에(google: sub) 하드 코딩하지 않기 위해서 userRequest 객체에서 가져온다
         */
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        /*
            Resource Server 에서 받아온 정보로 OAuth2Info 객체를 생성함
         */
        OAuth2Info oAuth2Info = OAuth2Info.of(registrationId, oAuth2UserAttributes);

        //OAuth2Info 객체로 MemberRepository 에 조회해 결과가 있으면 조회된 Member 객체를 반환하고 없으면 Member 객체를 생성
        Member member = getOrSave(oAuth2Info);
        return new PrincipalDetails(member, oAuth2UserAttributes);
    }

    private Member getOrSave(OAuth2Info oAuth2Info){
        Member member = memberRepository.findByEmail(oAuth2Info.getEmail())
                .orElseGet(oAuth2Info::toEntity);
        return memberRepository.save(member);
    }
}
