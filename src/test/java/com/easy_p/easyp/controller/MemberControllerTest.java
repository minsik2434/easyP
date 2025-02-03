package com.easy_p.easyp.controller;

import com.easy_p.easyp.common.jwt.JwtToken;
import com.easy_p.easyp.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {
    @InjectMocks
    MemberController memberController;
    @Mock
    MemberService memberService;
    ObjectMapper mapper = new ObjectMapper();
    MockMvc mockMvc;
    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    void auth2LoginTest() throws Exception {
        JwtToken jwtToken = new JwtToken("AccessToken", "RefreshToken");
        when(memberService.processOAuth2Login(any(String.class), any(String.class))).thenReturn(jwtToken);
        String authCode = "TestCode";
        mockMvc.perform(post("/member/oauth2/login")
                .contentType("application/json")
                .content(authCode))
                .andExpect(status().isOk());
    }
}