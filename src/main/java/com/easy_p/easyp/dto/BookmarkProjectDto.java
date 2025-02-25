package com.easy_p.easyp.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkProjectDto {
    private Long id;
    private ProjectDto projectDto;
    private Integer sequence;

    @Getter
    @Setter
    public static class ProjectDto{
        private Long id;
        private String name;
        private String imgUrl;

        @QueryProjection
        public ProjectDto(Long id, String name, String imgUrl){
            this.id = id;
            this.name = name;
            this.imgUrl = imgUrl;
        }
    }

    @QueryProjection
    public BookmarkProjectDto(Long id, ProjectDto projectDto, Integer sequence){
        this.id = id;
        this.projectDto = projectDto;
        this.sequence = sequence;
    }
}
