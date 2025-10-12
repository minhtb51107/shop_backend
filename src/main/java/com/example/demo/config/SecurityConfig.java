package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Cho phép truy cập công khai đến tất cả các endpoint của module 4
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/orders/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/returns/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/payments/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/warranty/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/promotions/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/order-status-histories/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/shipments/**")).permitAll()
                // Tất cả các request khác yêu cầu xác thực
                .anyRequest().authenticated()
            );
        return http.build();
    }
}