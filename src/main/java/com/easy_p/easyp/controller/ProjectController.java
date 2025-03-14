package com.easy_p.easyp.controller;

import com.easy_p.easyp.dto.MemberContext;
import com.easy_p.easyp.dto.PageDto;
import com.easy_p.easyp.dto.ParticipatingMember;
import com.easy_p.easyp.dto.ProjectDto;
import com.easy_p.easyp.dto.request.CreateProjectDto;
import com.easy_p.easyp.dto.request.InviteAcceptDto;
import com.easy_p.easyp.dto.request.InviteDto;
import com.easy_p.easyp.dto.response.AlarmDto;
import com.easy_p.easyp.service.AlarmService;
import com.easy_p.easyp.service.MemberService;
import com.easy_p.easyp.service.NotificationService;
import com.easy_p.easyp.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final AlarmService alarmService;
    @PostMapping
    public ResponseEntity<Void> createProject(@RequestPart("data")CreateProjectDto createProjectDto,
                                           @RequestPart("image")MultipartFile image,
                                           @AuthenticationPrincipal MemberContext context){
        projectService.saveProject(image,createProjectDto,context.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> projectInfo(@PathVariable("projectId") Long projectId,
                                                  @AuthenticationPrincipal MemberContext context){
        boolean isManager = context.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("MANAGER"::equals);

        if(!isManager){
            memberService.verifyingParticipatingProject(context.getUsername(), projectId);
        }
        ProjectDto project = projectService.getProject(projectId);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/invite")
    public ResponseEntity<Void> inviteProject(@RequestBody InviteDto inviteDto,
                                              @AuthenticationPrincipal MemberContext memberContext){
        AlarmDto alarmDto = projectService.genInviteCode(memberContext.getUsername(), inviteDto);
        alarmService.sendAlarm(inviteDto.getInviteeEmail(), alarmDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/invite/accept")
    public ResponseEntity<Void> inviteAccept(@RequestBody InviteAcceptDto inviteAcceptDto,
                                             @AuthenticationPrincipal MemberContext memberContext){
        notificationService.checkReadNotification(inviteAcceptDto.getNotificationId());
        projectService.inviteAccept(memberContext.getUsername(), inviteAcceptDto.getProjectId(), inviteAcceptDto.getInviteCode());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/{projectId}")
    public ResponseEntity<PageDto> members(@PathVariable("projectId") Long projectId,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @AuthenticationPrincipal MemberContext memberContext){
        log.info("{}", name);
        Pageable pageable = PageRequest.of(page, size);
        PageDto members = projectService.getMembers(projectId, memberContext.getUsername(), name, pageable);
        return ResponseEntity.ok(members);
    }
}
