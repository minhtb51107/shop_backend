package com.example.demo.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.user.dto.request.GoogleLoginRequest;
import com.example.demo.user.dto.request.LoginRequest;
import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.JwtResponse;
import com.example.demo.user.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterRequest request) {
        authService.registerCustomer(request);
        return ResponseEntity.ok("Đăng ký tài khoản khách hàng thành công!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse jwtResponse = authService.login(request);
        return ResponseEntity.ok(jwtResponse);
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Refresh token không được cung cấp.");
        }
        String refreshToken = authHeader.substring(7);
        JwtResponse jwtResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(jwtResponse);
    }
    
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@Valid @RequestBody GoogleLoginRequest request) {
        JwtResponse jwtResponse = authService.loginWithGoogle(request.getIdToken());
        return ResponseEntity.ok(jwtResponse);
    }
}