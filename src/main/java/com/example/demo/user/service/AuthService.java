package com.example.demo.user.service;

import com.example.demo.user.dto.request.LoginRequest;
import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.JwtResponse;

public interface AuthService {

    /**
     * Đăng ký một tài khoản khách hàng mới.
     * @param request DTO chứa thông tin đăng ký.
     */
    void registerCustomer(RegisterRequest request);

    /**
     * Xác thực người dùng và cấp token.
     * @param request DTO chứa email và mật khẩu.
     * @return JwtResponse chứa access và refresh token.
     */
    JwtResponse login(LoginRequest request);
    
    /**
     * Dùng refresh token để lấy access token mới.
     * @param refreshToken Chuỗi refresh token cũ.
     * @return JwtResponse chứa access token mới.
     */
    JwtResponse refreshToken(String refreshToken);
}