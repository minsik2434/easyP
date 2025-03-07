package com.easy_p.easyp.service;

import com.easy_p.easyp.dto.ProjectDto;
import com.easy_p.easyp.dto.request.CreateProjectDto;
import com.easy_p.easyp.dto.request.InviteDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {
    void saveProject(MultipartFile image, CreateProjectDto createProjectDto, String email);
    ProjectDto getProject(Long projectId);
    String genInviteCode(String inviterEmail, InviteDto inviteDto);
}
