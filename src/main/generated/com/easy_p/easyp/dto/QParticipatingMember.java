package com.easy_p.easyp.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.easy_p.easyp.dto.QParticipatingMember is a Querydsl Projection type for ParticipatingMember
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QParticipatingMember extends ConstructorExpression<ParticipatingMember> {

    private static final long serialVersionUID = 1511317176L;

    public QParticipatingMember(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> profile, com.querydsl.core.types.Expression<String> projectRole) {
        super(ParticipatingMember.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class}, id, name, email, profile, projectRole);
    }

}

