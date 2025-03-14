package com.easy_p.easyp.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InviteAcceptDto {
    private Long projectId;
    private Long notificationId;
    private String inviteCode;
}
