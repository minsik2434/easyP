package com.easy_p.easyp.repository.querydsl;

import com.easy_p.easyp.dto.PageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberQueryRepository {
    PageDto findParticipatingMemberByProjectId(Long projectId,String name, Pageable pageable);
}
