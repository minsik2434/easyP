package com.easy_p.easyp.service.project;

import com.easy_p.easyp.common.exception.BadRequestException;
import com.easy_p.easyp.common.exception.NotFoundException;
import com.easy_p.easyp.common.exception.PermissionException;
import com.easy_p.easyp.common.image.ImageManager;
import com.easy_p.easyp.common.store.InviteCodeStore;
import com.easy_p.easyp.dto.PageDto;
import com.easy_p.easyp.dto.ProjectDto;
import com.easy_p.easyp.dto.request.CreateProjectDto;
import com.easy_p.easyp.dto.request.InviteDto;
import com.easy_p.easyp.dto.response.AlarmDto;
import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.entity.Notification;
import com.easy_p.easyp.entity.Project;
import com.easy_p.easyp.entity.ProjectMember;
import com.easy_p.easyp.repository.MemberRepository;
import com.easy_p.easyp.repository.NotificationRepository;
import com.easy_p.easyp.repository.ProjectMemberRepository;
import com.easy_p.easyp.repository.ProjectRepository;
import com.easy_p.easyp.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final NotificationRepository notificationRepository;
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
    @Transactional
    public AlarmDto genInviteCode(String inviterEmail, InviteDto inviteDto) {
        ProjectMember inviter = projectMemberRepository.findByMemberEmailAndProjectId(inviterEmail, inviteDto.getProjectId())
                .orElseThrow(() -> new NotFoundException("Not Found"));
        if(!(inviter.getRole().equals("OWNER") || inviter.getRole().equals("MANAGER"))){
            throw new PermissionException("cannot invite unless OWNER, MANAGER");
        }

        Optional<ProjectMember> inviteeOptional = projectMemberRepository.findByMemberEmailAndProjectId(inviteDto.getInviteeEmail(), inviteDto.getProjectId());
        if(inviteeOptional.isPresent()){
            throw new BadRequestException("already in the Project");
        }
        String savedInviteCode = inviteCodeStore.get(inviteDto.getProjectId(), inviteDto.getInviteeEmail());
        if(!(savedInviteCode == null)){
            throw new BadRequestException("already invited Member");
        }
        String inviteCode = UUID.randomUUID().toString();
        inviteCodeStore.store(inviteDto.getProjectId(), inviteDto.getInviteeEmail(), inviteCode);
        Member member = memberRepository.findByEmail(inviteDto.getInviteeEmail())
                .orElseThrow(() -> new NotFoundException("Not Found"));
        Project project = projectRepository.findById(inviteDto.getProjectId()).orElseThrow(() -> new NotFoundException(("Not Found")));
        Notification notification = new Notification(project,member,"invite",inviteCode, false);
        notificationRepository.save(notification);
        return new AlarmDto("invite",project.getId(), inviter.getProject().getName());
    }

    @Override
    @Transactional
    public void inviteAccept(String inviteeEmail, Long projectId, String inviteCode) {
        String savedInviteCode = inviteCodeStore.get(projectId, inviteeEmail);
        if(savedInviteCode == null){
            throw new BadRequestException("inviteCode expiration");
        }
        if(!savedInviteCode.equals(inviteCode)){
            throw new BadRequestException("Not Matched InviteCode");
        }
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Not Found"));
        Member member = memberRepository.findByEmail(inviteeEmail).orElseThrow(() -> new NotFoundException("Not Found"));
        ProjectMember projectMember = new ProjectMember(project, member, "MEMBER");
        projectMemberRepository.save(projectMember);
        inviteCodeStore.delete(projectId, inviteeEmail);
    }

    @Override
    public PageDto getMembers(Long projectId, String email, String name, Pageable pageable) {
        Optional<ProjectMember> requestMember = projectMemberRepository.findByMemberEmailAndProjectId(email, projectId);
        if (requestMember.isEmpty()){
            throw new PermissionException("not involved in this project");
        }
        return memberRepository.findParticipatingMemberByProjectId(projectId, name, pageable);
    }
}
