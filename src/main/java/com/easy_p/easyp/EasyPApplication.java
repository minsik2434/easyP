package com.easy_p.easyp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EasyPApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyPApplication.class, args);
    }

}

