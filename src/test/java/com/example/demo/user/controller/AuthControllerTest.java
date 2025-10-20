package com.example.demo.user.controller;

import com.example.demo.config.security.UserDetailsServiceImpl; // Đảm bảo import đúng
import com.example.demo.shared.util.JwtUtil; // Đảm bảo import đúng
import com.example.demo.user.dto.request.LoginRequest;
import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.JwtResponse;
import com.example.demo.user.service.AuthService;
import com.example.demo.shared.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// IMPORT DÒNG NÀY ĐỂ SỬA LỖI 403
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.hamcrest.Matchers.is;


@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    // Các MockBean chúng ta đã thêm để sửa lỗi context
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test Case: TC_AUTH_01
    @Test
    public void whenLogin_givenValidCredentials_shouldReturnJwtToken() throws Exception {
        // 1. Given
        LoginRequest loginRequest = new LoginRequest("minhbinh51107@gmail.com", "0matkhau");
        // Bạn đã sửa đúng theo DTO của mình
        JwtResponse jwtResponse = new JwtResponse("mock-jwt-token", null, null);

        // Bạn đã sửa đúng theo Service của mình
        given(authService.login(any(LoginRequest.class), any())).willReturn(jwtResponse);

        // 2. When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf())) // <-- THÊM DÒNG NÀY ĐỂ SỬA LỖI 403
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("mock-jwt-token")));
    }

    // Test Case: TC_AUTH_02
    @Test
    public void whenLogin_givenInvalidCredentials_shouldReturnUnauthorized() throws Exception {
        // 1. Given
        LoginRequest loginRequest = new LoginRequest("minhbinh51107@gmail.com", "0matkhau");

        given(authService.login(any(LoginRequest.class), any()))
            .willThrow(new BadRequestException("Invalid credentials"));

        // 2. When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf())) // <-- THÊM DÒNG NÀY ĐỂ SỬA LỖI 403
                .andExpect(status().isBadRequest());
    }

    // Test Case: TC_AUTH_05
    @Test
    public void whenRegister_givenValidRequest_shouldReturnCreated() throws Exception {
        // 1. Given
        RegisterRequest registerRequest = new RegisterRequest("New User", "newuser@gmail.com", "123456", null);

        // Bạn đã sửa đúng tên hàm
        doNothing().when(authService).registerCustomer(any(RegisterRequest.class));

        // 2. When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
                .with(csrf())) // <-- THÊM DÒNG NÀY ĐỂ SỬA LỖI 403
                .andExpect(status().isCreated());
    }

    // Test Case: TC_AUTH_06
    @Test
    public void whenRegister_givenDuplicateEmail_shouldReturnBadRequest() throws Exception {
        // 1. Given
        RegisterRequest registerRequest = new RegisterRequest("Another User", "admin@example.com", "123456", null);
        
        doThrow(new BadRequestException("Email already exists"))
            .when(authService).registerCustomer(any(RegisterRequest.class));

        // 2. When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
                .with(csrf())) // <-- THÊM DÒNG NÀY ĐỂ SỬA LỖI 403
                .andExpect(status().isBadRequest());
    }
}