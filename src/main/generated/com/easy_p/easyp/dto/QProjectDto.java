package com.easy_p.easyp.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.easy_p.easyp.dto.QProjectDto is a Querydsl Projection type for ProjectDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProjectDto extends ConstructorExpression<ProjectDto> {

    private static final long serialVersionUID = -1398783613L;

    public QProjectDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<String> imgUrl, com.querydsl.core.types.Expression<? extends ProjectDto.Owner> owner, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updateAt) {
        super(ProjectDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, ProjectDto.Owner.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, name, description, imgUrl, owner, createAt, updateAt);
    }

}

