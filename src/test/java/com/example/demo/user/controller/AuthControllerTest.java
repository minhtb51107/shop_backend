//package com.example.demo.user.controller;
//
//import com.example.demo.config.security.JwtAuthenticationFilter;
//import com.example.demo.config.security.UserDetailsServiceImpl;
//import com.example.demo.shared.exception.BadRequestException;
//import com.example.demo.shared.util.JwtUtil;
//import com.example.demo.user.dto.request.LoginRequest;
//import com.example.demo.user.dto.request.RegisterRequest;
//import com.example.demo.user.dto.response.JwtResponse;
//import com.example.demo.user.service.AuthService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Test cho AuthController.
// * Vì các endpoint này là public, chúng ta sẽ vô hiệu hóa hoàn toàn Spring Security
// * để chỉ tập trung vào việc test logic của Controller.
// */
//@WebMvcTest(
//    controllers = AuthController.class,
//    // Vô hiệu hóa hoàn toàn các cấu hình tự động của Spring Security
//    excludeAutoConfiguration = {
//        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//        org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class
//    },
//    // Cũng loại bỏ JwtAuthenticationFilter khỏi context của test
//    excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
//    }
//)
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthService authService;
//
//    // Các MockBean này vẫn cần thiết vì @WebMvcTest không quét chúng,
//    // nhưng các thành phần khác có thể cần. An toàn nhất là giữ lại.
//    @MockBean
//    private JwtUtil jwtUtil;
//
//    @MockBean
//    private UserDetailsServiceImpl userDetailsService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void whenLogin_givenValidCredentials_shouldReturnJwtToken() throws Exception {
//        LoginRequest loginRequest = new LoginRequest("minhbinh51107@gmail.com", "0matkhau");
//        JwtResponse jwtResponse = new JwtResponse("mock-jwt-token", "mock-refresh-token", "Bearer");
//        given(authService.login(any(LoginRequest.class), any())).willReturn(jwtResponse);
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest))
//                        .with(csrf())) // Cần thiết nếu CSRF được bật
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken", is("mock-jwt-token")));
//    }
//
//    @Test
//    public void whenLogin_givenInvalidCredentials_shouldReturnBadRequest() throws Exception {
//        LoginRequest loginRequest = new LoginRequest("minhbinh51107@gmail.com", "wrongpass");
//        given(authService.login(any(LoginRequest.class), any()))
//                .willThrow(new BadRequestException("Invalid credentials"));
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest))
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void whenRegister_givenValidRequest_shouldReturnOk() throws Exception {
//        RegisterRequest registerRequest = new RegisterRequest("New User", "newuser@gmail.com", "password123", "0909123456");
//        doNothing().when(authService).registerCustomer(any(RegisterRequest.class));
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registerRequest))
//                        .with(csrf()))
//                .andExpect(status().isOk()); // API của bạn trả về ResponseEntity.ok() là 200
//    }
//
//    @Test
//    public void whenRegister_givenDuplicateEmail_shouldReturnBadRequest() throws Exception {
//        RegisterRequest registerRequest = new RegisterRequest("Another User", "admin@example.com", "password123", "0909654321");
//        doThrow(new BadRequestException("Email already exists"))
//                .when(authService).registerCustomer(any(RegisterRequest.class));
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registerRequest))
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//}
//
