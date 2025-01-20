package com.easy_p.easyp.config.security;

import com.easy_p.easyp.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Info {
    String name;
    String email;
    String profile;

    @Builder
    public OAuth2Info(String name, String email, String profile){
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public static OAuth2Info of(String registrationId, Map<String, Object> attributes){
        return
                switch (registrationId){
                    case "google" -> ofGoogle(attributes);
                    default -> throw new IllegalStateException("Unexpected value: " + registrationId);
                };
    }

    private static OAuth2Info ofGoogle(Map<String, Object> attributes){
        return OAuth2Info.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .profile((String) attributes.get("profile")).build();
    }

    public Member toEntity(){
        return new Member(email,name,"MEMBER",profile);
    }
}
