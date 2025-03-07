package com.easy_p.easyp.service.project;

import com.easy_p.easyp.common.exception.BadRequestException;
import com.easy_p.easyp.common.exception.NotFoundException;
import com.easy_p.easyp.common.exception.PermissionException;
import com.easy_p.easyp.common.image.ImageManager;
import com.easy_p.easyp.common.store.InviteCodeStore;
import com.easy_p.easyp.dto.ProjectDto;
import com.easy_p.easyp.dto.projection.ProjectMemberRoleProjection;
import com.easy_p.easyp.dto.request.CreateProjectDto;
import com.easy_p.easyp.dto.request.InviteDto;
import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.entity.Project;
import com.easy_p.easyp.entity.ProjectMember;
import com.easy_p.easyp.repository.MemberRepository;
import com.easy_p.easyp.repository.ProjectMemberRepository;
import com.easy_p.easyp.repository.ProjectRepository;
import com.easy_p.easyp.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final InviteCodeStore inviteCodeStore;
    private final ImageManager imageManager;

    @Override
    @Transactional
    public void saveProject(MultipartFile image, CreateProjectDto createProjectDto, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Not Found"));
        String savedImageUrl = imageManager.saveImage(image);
        Project project =
                new Project(createProjectDto.getTitle(), createProjectDto.getDescription(), savedImageUrl);

        ProjectMember projectMember = new ProjectMember(project, member, "OWNER");
        projectRepository.save(project);
        projectMemberRepository.save(projectMember);
    }

    @Override
    public ProjectDto getProject(Long projectId) {
        return projectRepository.findProjectByProjectId(projectId);
    }

    @Override
    public String genInviteCode(String inviterEmail, InviteDto inviteDto) {

        ProjectMemberRoleProjection inviterRole = projectMemberRepository.findRoleByMemberEmailAndProjectId(inviterEmail, inviteDto.getProjectId()).orElseThrow(
                () -> new NotFoundException("Not Found"));
        if(!(inviterRole.getRole().equals("OWNER") || inviterRole.getRole().equals("MANAGER"))){
            throw new PermissionException("cannot invite unless OWNER, MANAGER");
        }

        Optional<ProjectMember> inviteeOptional = projectMemberRepository.findByMemberEmailAndProjectId(inviteDto.getInviteeEmail(), inviteDto.getProjectId());
        if(inviteeOptional.isPresent()){
            throw new BadRequestException("already in the Project");
        }

        String inviteCode = UUID.randomUUID().toString();
        inviteCodeStore.store(inviteDto.getProjectId(), inviteDto.getInviteeEmail(), inviteCode);
        return inviteCode;
    }
}
