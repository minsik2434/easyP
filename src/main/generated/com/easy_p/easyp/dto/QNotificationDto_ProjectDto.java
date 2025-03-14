package com.easy_p.easyp.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.easy_p.easyp.dto.QNotificationDto_ProjectDto is a Querydsl Projection type for ProjectDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QNotificationDto_ProjectDto extends ConstructorExpression<NotificationDto.ProjectDto> {

    private static final long serialVersionUID = 1417668829L;

    public QNotificationDto_ProjectDto(com.querydsl.core.types.Expression<Long> projectId, com.querydsl.core.types.Expression<String> imgUrl, com.querydsl.core.types.Expression<String> name) {
        super(NotificationDto.ProjectDto.class, new Class<?>[]{long.class, String.class, String.class}, projectId, imgUrl, name);
    }

}

