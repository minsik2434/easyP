package com.easy_p.easyp.service;

import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.dto.MemberAuthDto;
import com.easy_p.easyp.dto.response.MemberInfo;
import com.easy_p.easyp.dto.PageDto;
import org.springframework.data.domain.Pageable;


public interface MemberService {
    MemberAuthDto processOAuth2Login(String authType, String authCode);
    String generateOAuth2AuthorizationUrl(String authType);
    JwtToken processTokenRefresh(String refreshToken);
    MemberInfo getMemberInfo(String email);
    PageDto getBelongProject(String email, String name, Pageable pageable);
    void saveBookmark(String email, Long projectId);
    PageDto getBookmarkingProject(String email, Pageable pageable);
    void updateBookmarkSequence(Long bookmarkId, Integer changeSequence, String email);
    void deleteBookmark(String email, Long bookmarkId);
}
