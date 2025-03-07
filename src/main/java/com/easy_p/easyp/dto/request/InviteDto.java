package com.easy_p.easyp.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InviteDto {
    private Long projectId;
    private String inviteeEmail;
}
