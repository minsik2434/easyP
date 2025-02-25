package com.easy_p.easyp.repository.querydsl;

import com.easy_p.easyp.dto.PageDto;
import org.springframework.data.domain.Pageable;

public interface BookmarkQueryRepository {
    PageDto  findBookmarkedProjectsByMemberEmail(String email, Pageable pageable);
}
