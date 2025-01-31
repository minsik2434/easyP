package com.easy_p.easyp.common.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class KakaoUserInfo implements UserInfo {
    private String id;
    private String email;
    private String nickname;
    private String profileImage;

    @Override
    public String getName() {
        return nickname;
    }

    @Override
    public String getProfile() {
        return profileImage;
    }
}
