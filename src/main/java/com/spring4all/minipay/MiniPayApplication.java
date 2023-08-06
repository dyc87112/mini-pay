package com.spring4all.minipay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableJpaAuditing
@SpringBootApplication
public class MiniPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniPayApplication.class, args);
    }

}
