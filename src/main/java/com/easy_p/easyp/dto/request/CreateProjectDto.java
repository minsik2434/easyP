package com.easy_p.easyp.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateProjectDto {
    private String title;
    private String description;
}
