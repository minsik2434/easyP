package com.easy_p.easyp.config.security;

import com.easy_p.easyp.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class PrincipalDetails implements UserDetails, OAuth2User {
    private Member member;
    private Map<String, Object> attribute;

    public PrincipalDetails(Member member){
        this.member = member;
    }

    public PrincipalDetails(Member member, Map<String, Object> attribute){
        this.member = member;
        this.attribute = attribute;
    }
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attribute;
    }
}
