package com.easy_p.easyp.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.easy_p.easyp.dto.QProjectDto_Owner is a Querydsl Projection type for Owner
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProjectDto_Owner extends ConstructorExpression<ProjectDto.Owner> {

    private static final long serialVersionUID = -1903443352L;

    public QProjectDto_Owner(com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> profile) {
        super(ProjectDto.Owner.class, new Class<?>[]{String.class, String.class}, email, profile);
    }

}

