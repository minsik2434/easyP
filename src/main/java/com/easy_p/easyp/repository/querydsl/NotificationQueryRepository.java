package com.easy_p.easyp.repository.querydsl;

import com.easy_p.easyp.dto.PageDto;
import org.springframework.data.domain.Pageable;

public interface NotificationQueryRepository {
    PageDto findByMemberEmailAndSearch(String email, String search, Pageable pageable);
}
