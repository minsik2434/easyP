package com.easy_p.easyp.service;

import com.easy_p.easyp.dto.PageDto;
import com.easy_p.easyp.dto.ProjectDto;
import com.easy_p.easyp.dto.request.CreateProjectDto;
import com.easy_p.easyp.dto.request.InviteDto;
import com.easy_p.easyp.dto.response.AlarmDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {
    void saveProject(MultipartFile image, CreateProjectDto createProjectDto, String email);
    ProjectDto getProject(Long projectId);
    AlarmDto genInviteCode(String inviterEmail, InviteDto inviteDto);
    void inviteAccept(String inviteeEmail,Long projectId, String inviteCode);
    PageDto getMembers(Long projectId, String email, String name,  Pageable pageable);
}
