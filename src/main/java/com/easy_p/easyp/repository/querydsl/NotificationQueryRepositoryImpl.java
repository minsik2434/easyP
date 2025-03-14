package com.easy_p.easyp.repository.querydsl;

import com.easy_p.easyp.dto.NotificationDto;
import com.easy_p.easyp.dto.PageDto;
import com.easy_p.easyp.dto.QNotificationDto;
import com.easy_p.easyp.dto.QNotificationDto_ProjectDto;
import com.easy_p.easyp.entity.QMember;
import com.easy_p.easyp.entity.QNotification;
import com.easy_p.easyp.entity.QProject;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.easy_p.easyp.entity.QMember.member;
import static com.easy_p.easyp.entity.QNotification.notification;
import static com.easy_p.easyp.entity.QProject.project;

@Repository
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository{
    private final JPAQueryFactory queryFactory;
    QNotification qNotification = notification;
    QMember qMember = member;
    QProject qProject = project;
    public NotificationQueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    
    @Override
    public PageDto findByMemberEmailAndSearch(String email, String search, Pageable pageable) {
        BooleanBuilder whereBuilder = createWhereBuilder(email, search);
        List<NotificationDto> content = queryFactory
                .select(
                        new QNotificationDto(
                                notification.id,
                                new QNotificationDto_ProjectDto(
                                        project.id,
                                        project.imageUrl,
                                        project.name
                                ),
                                notification.type,
                                notification.content,
                                notification.isRead,
                                notification.createAt
                        )
                )
                .from(notification)
                .join(notification.member, member)
                .join(notification.project, project)
                .where(whereBuilder)
                .orderBy(notification.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(Wildcard.count)
                .from(notification)
                .join(notification.member, member)
                .join(notification.project, project)
                .where(whereBuilder)
                .fetchOne();

        long totalPage = totalCount / pageable.getPageSize();
        if(totalCount % pageable.getPageSize() > 0){
            totalPage++;
        }
        return new PageDto(content, pageable.getPageNumber(), totalPage, pageable.getPageSize(), totalCount);
    }

    private BooleanBuilder createWhereBuilder(String email, String search){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(member.email.eq(email));
        if(search.equalsIgnoreCase("noRead")){
            builder.and(notification.isRead.eq(false));
        }
        return builder;
    }
}
