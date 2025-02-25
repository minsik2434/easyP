package com.easy_p.easyp.service.member;

import com.easy_p.easyp.common.exception.BadRequestException;
import com.easy_p.easyp.common.exception.JwtTokenException;
import com.easy_p.easyp.common.exception.NotFoundException;
import com.easy_p.easyp.common.jwt.JwtProvider;
import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.common.jwt.RefreshTokenStore;
import com.easy_p.easyp.common.oauth2.OAuthManager;
import com.easy_p.easyp.common.oauth2.dto.UserInfo;
import com.easy_p.easyp.dto.MemberContext;
import com.easy_p.easyp.dto.MemberAuthDto;
import com.easy_p.easyp.dto.response.MemberInfo;
import com.easy_p.easyp.dto.PageDto;
import com.easy_p.easyp.entity.Bookmark;
import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.entity.Project;
import com.easy_p.easyp.entity.ProjectMember;
import com.easy_p.easyp.repository.BookmarkRepository;
import com.easy_p.easyp.repository.MemberRepository;
import com.easy_p.easyp.repository.ProjectMemberRepository;
import com.easy_p.easyp.repository.ProjectRepository;
import com.easy_p.easyp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final OAuthManager oAuthManager;
    private final RefreshTokenStore refreshTokenStore;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    @Override
    public MemberAuthDto processOAuth2Login(String authType, String authCode) {
        JwtToken jwtToken;
        MemberAuthDto memberAuthDto;
        UserInfo userInfo = oAuthManager.getUserInfo(authType, authCode);
        String email = userInfo.getEmail();
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if(optionalMember.isPresent()) {
            jwtToken = jwtProvider.createToken(email);
            memberAuthDto = buildUserAuthDto(optionalMember.get(),jwtToken);
        }
        else{
            Member member = new Member(email, userInfo.getName(), "MEMBER", userInfo.getProfile());
            Member save = memberRepository.save(member);
            jwtToken = jwtProvider.createToken(save.getEmail());
            memberAuthDto = buildUserAuthDto(save, jwtToken);
        }
        refreshTokenStore.storeRefreshToken(email, jwtToken.getRefreshToken());
        return memberAuthDto;
    }

    @Override
    public String generateOAuth2AuthorizationUrl(String authType) {
        return oAuthManager.getAuthRequestUri(authType);
    }

    @Override
    public JwtToken processTokenRefresh(String refreshToken) {

        jwtProvider.validateToken(refreshToken);

        String email = jwtProvider.getClaim(refreshToken, "email");
        String savedToken = refreshTokenStore.getRefreshToken(email);
        if(savedToken == null || !savedToken.equals(refreshToken)){
            throw new JwtTokenException("Invalid Refresh Token");
        }
        JwtToken token = jwtProvider.createToken(email);
        refreshTokenStore.storeRefreshToken(email, token.getRefreshToken());
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo getMemberInfo(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new NotFoundException("NotFound"));
        return new MemberInfo(member);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto getBelongProject(String email, Pageable pageable) {
        return projectRepository.findProjectListByEmail(email, pageable);
    }

    @Override
    @Transactional
    public void saveBookmark(String email, Long projectId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Not Found"));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Not Found"));
        Optional<ProjectMember> belongProject = projectMemberRepository.findByProjectAndMember(project, member);
        if(belongProject.isEmpty()){
            throw new NotFoundException("It's not a project you're involved in");
        }
        Optional<Bookmark> bookmark = bookmarkRepository.findByMemberAndProject(member, project);
        if(bookmark.isPresent()){
            throw new BadRequestException("already existing");
        }
        int sequence = bookmarkRepository.countMemberBookmarkProject(email).intValue();
        bookmarkRepository.save(new Bookmark(project, member , sequence+1));
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto getBookmarkingProject(String email, Pageable pageable) {
        return bookmarkRepository.findBookmarkedProjectsByMemberEmail(email, pageable);
    }

    @Override
    @Transactional
    public void updateBookmarkSequence(Long bookmarkId, Integer changeSequence, String email) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new NotFoundException("Not Found"));
        if(Objects.equals(bookmark.getSequence(), changeSequence)){
            throw new BadRequestException("Bad Request");
        }

       if(bookmark.getSequence() < changeSequence){
            bookmarkRepository.decreaseBookmarkSequence(email, bookmark.getSequence(), changeSequence);
            bookmark.setSequence(changeSequence);
       }
       else {
           bookmarkRepository.increaseBookmarkSequence(email, bookmark.getSequence(), changeSequence);
           bookmark.setSequence(changeSequence);
       }
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> optional = memberRepository.findByEmail(email);
        if(optional.isEmpty()){
            throw new NotFoundException("Not Found Member");
        }
        Member member = optional.get();
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(optional.get().getRole()));
        return new MemberContext(member, authorities);
    }

    private MemberAuthDto buildUserAuthDto(Member member, JwtToken jwtToken){
        return new MemberAuthDto(
                member.getEmail(),
                jwtToken,
                member.getRole(),
                member.getName(),
                member.getProfile()
        );
    }
}
