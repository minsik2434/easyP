package com.easy_p.easyp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String test;
    @GetMapping("/test")
    public String test(){
        return test;
    }
}
