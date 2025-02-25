package com.easy_p.easyp.repository.querydsl;

import com.easy_p.easyp.dto.ProjectDto;
import com.easy_p.easyp.dto.PageDto;
import com.easy_p.easyp.dto.QProjectDto;
import com.easy_p.easyp.dto.QProjectDto_Owner;
import com.easy_p.easyp.entity.Project;
import com.easy_p.easyp.entity.QMember;
import com.easy_p.easyp.entity.QProject;
import com.easy_p.easyp.entity.QProjectMember;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.easy_p.easyp.entity.QMember.member;
import static com.easy_p.easyp.entity.QProject.project;
import static com.easy_p.easyp.entity.QProjectMember.projectMember;

@Repository
@Slf4j
public class ProjectQueryRepositoryImpl implements ProjectQueryRepository{
    QProject qProject = project;
    QProjectMember qProjectMember = projectMember;
    QMember qMember = member;
    private final JPAQueryFactory queryFactory;
    public ProjectQueryRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public PageDto findProjectListByEmail(String email, Pageable pageable) {
        QProjectMember ownerProjectMember = new QProjectMember("ownerProjectMember");
        QMember owner = new QMember("owner");
        List<ProjectDto> content = queryFactory
                .select(
                        new QProjectDto(
                                project.id,
                                project.name,
                                project.description,
                                project.imageUrl,
                                new QProjectDto_Owner(owner.email, owner.profile),
                                project.createAt,
                                project.updateAt
                        )
                )
                .from(projectMember)
                .join(projectMember.project, project)
                .join(projectMember.member, member)
                .join(ownerProjectMember).on(ownerProjectMember.project.eq(project))
                .join(ownerProjectMember.member, owner)
                .where(member.email.eq(email), ownerProjectMember.role.eq("OWNER"))
                .orderBy(createOrderSpecifierForProject(pageable, project))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).distinct().fetch();

        Long totalCount = queryFactory.select(Wildcard.count)
                .from(projectMember)
                .join(projectMember.project, project)
                .join(projectMember.member, member)
                .where(member.email.eq(email)).fetchOne();
        long totalPage = totalCount / pageable.getPageSize();
        if (totalCount % pageable.getPageSize() > 0) {
            totalPage++;
        }
        return new PageDto(content, pageable.getPageNumber(), totalPage, pageable.getPageSize(), totalCount);
    }

    private OrderSpecifier<?> createOrderSpecifierForProject(Pageable pageable, QProject project){
        if(pageable.getSort().isEmpty()){
            return project.id.asc();
        }

        Sort.Order order = pageable.getSort().iterator().next();
        PathBuilder<?> pathBuilder = new PathBuilder<>(Project.class, project.getMetadata());
        return new OrderSpecifier<>(
                order.isAscending() ? Order.ASC : Order.DESC,
                pathBuilder.getComparable(order.getProperty(), String.class)
        );
    }
}
