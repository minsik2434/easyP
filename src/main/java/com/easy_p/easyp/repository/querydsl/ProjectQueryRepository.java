package com.easy_p.easyp.repository.querydsl;

import com.easy_p.easyp.dto.PageDto;
import com.easy_p.easyp.dto.ProjectDto;
import org.springframework.data.domain.Pageable;

public interface ProjectQueryRepository {
    PageDto findProjectListByEmail(String email, String name, Pageable pageable);
    ProjectDto findProjectByProjectId(Long projectId);
}
