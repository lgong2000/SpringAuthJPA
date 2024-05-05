package com.example.springauthjpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.ignoringAntMatchers("/h2-console/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/api/posts").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().sameOrigin())
                .build();
    }
}
