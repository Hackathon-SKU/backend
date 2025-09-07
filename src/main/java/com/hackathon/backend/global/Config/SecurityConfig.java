package com.hackathon.backend.global.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/user/login", "/user/join", "/swagger-ui/**", "/api-docs", "swagger-ui-custom.html"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().permitAll());
        return http.build();
    }
}
