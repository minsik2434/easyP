package com.easy_p.easyp.repository;

import com.easy_p.easyp.dto.projection.ProjectMemberRoleProjection;
import com.easy_p.easyp.entity.Project;
import com.easy_p.easyp.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    long countByProject(Project project);;
    @Query("SELECT pm FROM ProjectMember pm WHERE pm.member.email = :email AND pm.project.id =:projectId")
    Optional<ProjectMember> findByMemberEmailAndProjectId(@Param("email") String email, @Param("projectId") Long projectId);

    @Query("SELECT pm.role as role FROM ProjectMember pm JOIN pm.member m JOIN pm.project p WHERE m.email = :email AND p.id = :projectId")
    Optional<ProjectMemberRoleProjection> findRoleByMemberEmailAndProjectId(@Param("email") String email,
                                                                            @Param("projectId") Long projectId);

}
