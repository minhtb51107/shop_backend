package com.example.demo.user.service;

import com.example.demo.user.dto.request.ChangePasswordRequest;
import com.example.demo.user.dto.request.ForgotPasswordRequest;
import com.example.demo.user.dto.request.LoginRequest;
import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.request.ResetPasswordRequest;
import com.example.demo.user.dto.response.JwtResponse;
import com.example.demo.user.dto.response.UserDetailsResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    /**
     * Đăng ký một tài khoản khách hàng mới.
     * @param request DTO chứa thông tin đăng ký.
     */
    void registerCustomer(RegisterRequest request);

    /**
     * Kích hoạt tài khoản người dùng bằng token.
     * @param token Chuỗi token kích hoạt.
     */
    void activateUserAccount(String token); // <-- THÊM PHƯƠNG THỨC NÀY

    /**
     * Xác thực người dùng và cấp token.
     * @param request DTO chứa email và mật khẩu.
     * @param servletRequest Đối tượng HttpServletRequest để lấy IP và User-Agent.
     * @return JwtResponse chứa access và refresh token.
     */
    JwtResponse login(LoginRequest request, HttpServletRequest servletRequest); // <-- THAY ĐỔI CHỮ KÝ PHƯƠNG THỨC
    
    /**
     * Dùng refresh token để lấy access token mới.
     * @param refreshToken Chuỗi refresh token cũ.
     * @return JwtResponse chứa access token mới.
     */
    JwtResponse refreshToken(String refreshToken);
    
    /**
     * Xử lý yêu cầu quên mật khẩu. Tạo token và gửi email.
     * @param request DTO chứa email của người dùng.
     */
    void forgotPassword(ForgotPasswordRequest request);

    /**
     * Reset mật khẩu người dùng bằng token hợp lệ.
     * @param request DTO chứa token và mật khẩu mới.
     */
    void resetPassword(ResetPasswordRequest request);
    
    /**
     * Thay đổi mật khẩu cho người dùng đang đăng nhập.
     * @param request DTO chứa mật khẩu cũ và mới.
     * @param userEmail Email của người dùng đang thực hiện yêu cầu (lấy từ SecurityContext).
     */
    void changePassword(ChangePasswordRequest request, String userEmail);

    /**
     * Lấy thông tin chi tiết của người dùng đang đăng nhập.
     * @param userEmail Email của người dùng đang thực hiện yêu cầu.
     * @return DTO chứa thông tin chi tiết của người dùng.
     */
    UserDetailsResponse getCurrentUserDetails(String userEmail); // <-- THÊM PHƯƠNG THỨC NÀY
    
    /**
     * Đăng xuất người dùng bằng cách vô hiệu hóa refresh token.
     * @param refreshToken Chuỗi refresh token cần vô hiệu hóa.
     */
    void logout(String refreshToken); // <-- THÊM PHƯƠNG THỨC NÀY
    
    /**
     * Xác thực người dùng bằng Google ID Token và cấp token của hệ thống.
     * @param idTokenString ID Token từ Google.
     * @param servletRequest Đối tượng HttpServletRequest để lấy IP và User-Agent.
     * @return JwtResponse chứa access và refresh token.
     */
    JwtResponse loginWithGoogle(String idTokenString, HttpServletRequest servletRequest); // <-- THAY ĐỔI CHỮ KÝ
}