package com.easy_p.easyp.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipatingMember {
    private Long id;
    private String name;
    private String email;
    private String profile;
    private String projectRole;

    @QueryProjection
    public ParticipatingMember(Long id, String name, String email, String profile, String projectRole){
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.projectRole = projectRole;
    }
}
