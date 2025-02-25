package com.easy_p.easyp.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private String imgUrl;
    private Owner owner;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @QueryProjection
    public ProjectDto(Long id, String name, String description, String imgUrl, Owner owner, LocalDateTime createAt, LocalDateTime updateAt){
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.owner = owner;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    @Getter
    @Setter
    @ToString
    public static class Owner{
        private String email;
        private String profile;
        @QueryProjection
        public Owner(String email, String profile){
            this.email = email;
            this.profile = profile;
        }
    }
}
