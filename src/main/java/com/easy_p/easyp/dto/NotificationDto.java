package com.easy_p.easyp.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private ProjectDto projectDto;
    private String type;
    private String content;
    private LocalDateTime createAt;
    private boolean isRead;

    @Getter
    @Setter
    public static class ProjectDto {
        private Long projectId;
        private String imgUrl;
        private String name;

        @QueryProjection
        public ProjectDto(Long projectId, String imgUrl, String name){
            this.projectId = projectId;
            this.imgUrl = imgUrl;
            this.name = name;
        }
    }

    @QueryProjection
    public NotificationDto(Long id, ProjectDto projectDto, String type, String content, boolean isRead, LocalDateTime createAt){
        this.id = id;
        this.projectDto = projectDto;
        this.type = type;
        this.content = content;
        this.isRead = isRead;
        this.createAt = createAt;
    }
}
