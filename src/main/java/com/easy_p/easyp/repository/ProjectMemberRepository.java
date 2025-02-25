package com.easy_p.easyp.repository;

import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.entity.Project;
import com.easy_p.easyp.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectAndMember(Project project, Member member);
}
