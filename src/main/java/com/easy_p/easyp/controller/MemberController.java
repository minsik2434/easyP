package com.easy_p.easyp.controller;

import com.easy_p.easyp.dto.Auth2Login;
import com.easy_p.easyp.service.MemberService;
import com.easy_p.easyp.service.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    @PostMapping("/auth2/login")
    public void auth2Login(@RequestBody Auth2Login auth2Login){
        memberService.authentication(auth2Login);
    }
}
