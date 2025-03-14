package com.easy_p.easyp.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.easy_p.easyp.dto.QNotificationDto is a Querydsl Projection type for NotificationDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QNotificationDto extends ConstructorExpression<NotificationDto> {

    private static final long serialVersionUID = -2145469033L;

    public QNotificationDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<? extends NotificationDto.ProjectDto> projectDto, com.querydsl.core.types.Expression<String> type, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Boolean> isRead, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt) {
        super(NotificationDto.class, new Class<?>[]{long.class, NotificationDto.ProjectDto.class, String.class, String.class, boolean.class, java.time.LocalDateTime.class}, id, projectDto, type, content, isRead, createAt);
    }

}

