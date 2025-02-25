package com.easy_p.easyp.repository;

import com.easy_p.easyp.entity.Project;
import com.easy_p.easyp.repository.querydsl.ProjectQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> , ProjectQueryRepository {
}
