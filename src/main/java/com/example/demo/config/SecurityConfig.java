package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt tính năng CSRF (Cross-Site Request Forgery) - thường cần thiết cho REST APIs
                .csrf(csrf -> csrf.disable())

                // Đây là phần quan trọng nhất
                .authorizeHttpRequests(auth -> auth
                        // Cho phép TẤT CẢ các request đi qua mà không cần xác thực
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
