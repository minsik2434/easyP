package com.easy_p.easyp.entity;

import com.easy_p.easyp.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String password;
    private String role;
    private String profile;

    public Member(String email, String name, String role, String profile){
        this.email=email;
        this.name=name;
        this.role=role;
        this.profile = profile;
    }

    public Member(String email, String name, String role, String password, String profile){
        this.email = email;
        this.name = name;
        this.role = role;
        this.password = password;
        this.profile = profile;
    }
}
