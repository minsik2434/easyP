package com.easy_p.easyp.common.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GoogleUserInfo implements UserInfo{
    private String sub;
    private String email;
    private String name;
    private String picture;

    @Override
    public String getProfile() {
        return picture;
    }
}
