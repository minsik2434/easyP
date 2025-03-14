package com.easy_p.easyp.repository.querydsl;

import com.easy_p.easyp.dto.PageDto;
import com.easy_p.easyp.dto.ParticipatingMember;
import com.easy_p.easyp.dto.QParticipatingMember;
import com.easy_p.easyp.entity.QMember;
import com.easy_p.easyp.entity.QProjectMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.easy_p.easyp.entity.QMember.*;
import static com.easy_p.easyp.entity.QProjectMember.*;

@Repository
@Slf4j
public class MemberQueryRepositoryImpl implements MemberQueryRepository{
    private final JPAQueryFactory queryFactory;
    QProjectMember qProjectMember = projectMember;
    QMember qMember = member;
    public MemberQueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public PageDto findParticipatingMemberByProjectId(Long projectId, String name, Pageable pageable) {
        List<ParticipatingMember> content = queryFactory
                .select(
                        new QParticipatingMember(
                                projectMember.member.id,
                                projectMember.member.name,
                                projectMember.member.email,
                                projectMember.member.profile,
                                projectMember.role
                        )
                )
                .from(projectMember)
                .join(projectMember.member, member)
                .where(projectMember.project.id.eq(projectId), containMemberName(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();
        Long totalCount = queryFactory.select(Wildcard.count)
                .from(projectMember)
                .join(projectMember. member)
                .where(projectMember.project.id.eq(projectId), containMemberName(name)).fetchOne();
        long totalPage = totalCount / pageable.getPageSize();
        if (totalCount % pageable.getPageSize() > 0) {
            totalPage++;
        }
        return new PageDto(content, pageable.getPageNumber(), totalPage, pageable.getPageSize(), totalCount);
    }

    private BooleanExpression containMemberName(String name){
        if(name == null || name.isEmpty()){
            return null;
        }
        return projectMember.member.name.contains(name);
    }

}
