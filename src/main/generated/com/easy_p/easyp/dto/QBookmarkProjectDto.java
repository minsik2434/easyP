package com.easy_p.easyp.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.easy_p.easyp.dto.QBookmarkProjectDto is a Querydsl Projection type for BookmarkProjectDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBookmarkProjectDto extends ConstructorExpression<BookmarkProjectDto> {

    private static final long serialVersionUID = -880383399L;

    public QBookmarkProjectDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<? extends BookmarkProjectDto.ProjectDto> projectDto, com.querydsl.core.types.Expression<Integer> sequence) {
        super(BookmarkProjectDto.class, new Class<?>[]{long.class, BookmarkProjectDto.ProjectDto.class, int.class}, id, projectDto, sequence);
    }

}

