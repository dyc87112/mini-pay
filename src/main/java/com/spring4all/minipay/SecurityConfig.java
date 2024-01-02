package com.spring4all.minipay;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers("/wxpay/**").authenticated()
                .requestMatchers("/dashboard").authenticated()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/notify/**").permitAll()
                .and()
                .formLogin(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable());
        return http.build();
    }

}
