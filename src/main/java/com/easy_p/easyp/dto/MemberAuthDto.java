package com.easy_p.easyp.dto;

import com.easy_p.easyp.common.jwt.JwtToken;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class MemberAuthDto {
    private String email;
    private JwtToken jwtToken;
    private String role;
    private String name;
    private String profile;

}
