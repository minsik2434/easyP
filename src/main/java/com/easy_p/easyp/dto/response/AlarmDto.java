package com.easy_p.easyp.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlarmDto {
    private String type;
    private Long sourceProjectId;
    private String content;

    public AlarmDto(String type, Long sourceProjectId, String content){
        this.type = type;
        this.sourceProjectId = sourceProjectId;
        this.content = content;
    }
}
