package com.easy_p.easyp.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.easy_p.easyp.dto.QBookmarkProjectDto_ProjectDto is a Querydsl Projection type for ProjectDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBookmarkProjectDto_ProjectDto extends ConstructorExpression<BookmarkProjectDto.ProjectDto> {

    private static final long serialVersionUID = -495710501L;

    public QBookmarkProjectDto_ProjectDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> imgUrl) {
        super(BookmarkProjectDto.ProjectDto.class, new Class<?>[]{long.class, String.class, String.class}, id, name, imgUrl);
    }

}

