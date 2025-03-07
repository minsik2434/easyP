package com.easy_p.easyp.controller;

import com.easy_p.easyp.dto.MemberContext;
import com.easy_p.easyp.dto.ProjectDto;
import com.easy_p.easyp.dto.request.CreateProjectDto;
import com.easy_p.easyp.dto.request.InviteDto;
import com.easy_p.easyp.dto.response.AlarmDto;
import com.easy_p.easyp.service.AlarmService;
import com.easy_p.easyp.service.MemberService;
import com.easy_p.easyp.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    private final MemberService memberService;
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

        String inviteCode = projectService.genInviteCode(memberContext.getUsername(), inviteDto);

        alarmService.sendAlarm(inviteDto.getInviteeEmail(), new AlarmDto("invite", inviteDto.getProjectId(),inviteCode));
        return null;
    }
}
