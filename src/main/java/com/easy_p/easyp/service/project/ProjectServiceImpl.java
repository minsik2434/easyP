package com.easy_p.easyp.service.project;

import com.easy_p.easyp.common.exception.BadRequestException;
import com.easy_p.easyp.common.exception.NotFoundException;
import com.easy_p.easyp.entity.Bookmark;
import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.entity.Project;
import com.easy_p.easyp.entity.ProjectMember;
import com.easy_p.easyp.repository.BookmarkRepository;
import com.easy_p.easyp.repository.MemberRepository;
import com.easy_p.easyp.repository.ProjectMemberRepository;
import com.easy_p.easyp.repository.ProjectRepository;
import com.easy_p.easyp.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

}
