package com.easy_p.easyp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String email;
    private String name;
    private String profile;
    private String role;
    private String accessToken;
}
