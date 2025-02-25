package com.easy_p.easyp.dto.response;

import com.easy_p.easyp.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfo {
    private String email;
    private String name;
    private String profile;
    private String role;

    public MemberInfo(Member member){
        this.email = member.getEmail();
        this.name = member.getName();
        this.profile = member.getProfile();
        this.role = member.getRole();
    }
}
