package com.easy_p.easyp.repository.querydsl;

import com.easy_p.easyp.dto.*;
import com.easy_p.easyp.entity.QBookmark;
import com.easy_p.easyp.entity.QMember;
import com.easy_p.easyp.entity.QProject;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.easy_p.easyp.entity.QBookmark.bookmark;
import static com.easy_p.easyp.entity.QMember.member;
import static com.easy_p.easyp.entity.QProject.project;
@Repository
public class BookmarkQueryRepositoryImpl implements BookmarkQueryRepository{
    private final JPAQueryFactory queryFactory;
    QBookmark qBookmark = bookmark;
    QMember qMember = member;
    QProject qProject = project;
    public BookmarkQueryRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public PageDto findBookmarkedProjectsByMemberEmail(String email, Pageable pageable) {
        List<BookmarkProjectDto> content = queryFactory
                .select(
                        new QBookmarkProjectDto(
                                bookmark.id,
                                new QBookmarkProjectDto_ProjectDto(project.id, project.name, project.imageUrl),
                                bookmark.sequence)
                )
                .from(bookmark)
                .join(bookmark.member, member)
                .join(bookmark.project, project)
                .where(member.email.eq(email))
                .orderBy(bookmark.sequence.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(Wildcard.count)
                .from(bookmark)
                .join(bookmark.member,member)
                .where(member.email.eq(email))
                .fetchOne();

        long totalPage = totalCount / pageable.getPageSize();
        if(totalCount % pageable.getPageSize() > 0){
            totalPage++;
        }
        return new PageDto(content, pageable.getPageNumber(), totalPage, pageable.getPageSize(), totalCount);
    }
}
