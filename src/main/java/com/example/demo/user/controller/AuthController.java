package com.example.demo.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import com.example.demo.user.dto.request.*; // Using wildcard import for brevity
import com.example.demo.user.dto.response.JwtResponse;
import com.example.demo.user.dto.response.UserDetailsResponse;
import com.example.demo.user.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid; // <--- ADD THIS IMPORT
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder; // Thêm import này
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.authentication.AnonymousAuthenticationToken; // Thêm import này

import org.slf4j.Logger; // Thêm import logger
import org.slf4j.LoggerFactory; // Thêm import logger factory

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterRequest request) { // @Valid needs the import
        authService.registerCustomer(request);
        return ResponseEntity.ok("Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt tài khoản.");
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        authService.activateUserAccount(token);
        return ResponseEntity.ok("Tài khoản của bạn đã được kích hoạt thành công! Bây giờ bạn có thể đăng nhập.");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) { // @Valid needs the import
        JwtResponse jwtResponse = authService.login(request, servletRequest);
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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) { // @Valid needs the import
        authService.forgotPassword(request);
        return ResponseEntity.ok("Nếu email của bạn tồn tại trong hệ thống, bạn sẽ nhận được một liên kết để đặt lại mật khẩu.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) { // @Valid needs the import
        authService.resetPassword(request);
        return ResponseEntity.ok("Mật khẩu của bạn đã được đặt lại thành công.");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() { // Đổi kiểu trả về tạm thời để trả về lỗi dễ hơn
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check 1: Có authentication không?
        if (authentication == null) {
            log.warn("[/me] Authentication object is null.");
            // Trả về 401 Unauthorized thay vì để lỗi 500 hoặc 404 không rõ ràng
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Authentication required.");
        }

        // Check 2: User có phải là anonymous không?
        // Hoặc kiểm tra !authentication.isAuthenticated() cũng được
        if (authentication instanceof AnonymousAuthenticationToken) {
            log.warn("[/me] User is anonymous. Principal: {}", authentication.getPrincipal());
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                  .body("Valid authentication required (anonymous access denied).");
        }

        // Check 3: Đã xác thực nhưng principal lại không hợp lệ? (Hiếm khi xảy ra)
         if (!authentication.isAuthenticated() || authentication.getPrincipal() == null) {
             log.warn("[/me] User is not authenticated or principal is null despite not being anonymous. Principal: {}", authentication.getPrincipal());
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                  .body("Invalid authentication state.");
         }


        Object principal = authentication.getPrincipal();
        String userEmail = null;

        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            userEmail = (String) principal;
            // Double check nếu principal là string "anonymousUser" dù đã qua check ở trên
            if ("anonymousUser".equals(userEmail)) {
                 log.error("[/me] Principal is 'anonymousUser' string despite being authenticated!");
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                      .body("Security context error.");
            }
        } else {
             log.error("[/me] Unexpected principal type: {}", principal.getClass().getName());
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body("Server processing error.");
        }

        // Check 4: Email lấy ra có vấn đề?
        if (userEmail == null || userEmail.isEmpty()) {
            log.error("[/me] Extracted user email is null or empty. Principal: {}", principal);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body("Failed to extract user identifier.");
        }

        log.info("[/me] Fetching details for authenticated user: {}", userEmail); // Log email hợp lệ
        try {
            UserDetailsResponse userDetails = authService.getCurrentUserDetails(userEmail);
            // Nếu tìm thấy user, trả về 200 OK
            return ResponseEntity.ok(userDetails);
        } catch (ResourceNotFoundException ex) {
            // AuthService không tìm thấy user dù đã xác thực -> Lỗi logic/dữ liệu
            log.error("[/me] User authenticated as '{}' but not found in database.", userEmail, ex);
            // Trả về 500 Internal Server Error vì đây là trạng thái không mong đợi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("User data inconsistency.");
        } catch (Exception e) {
             log.error("[/me] Unexpected error fetching details for user '{}'.", userEmail, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error retrieving user details.");
        }
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request, // @Valid needs the import
            Authentication authentication) {
        String userEmail = authentication.getName();
        authService.changePassword(request, userEmail);
        return ResponseEntity.ok("Đổi mật khẩu thành công.");
    }

    @PostMapping("/logout")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest request) { // @Valid needs the import
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Đăng xuất thành công.");
    }

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(
            @Valid @RequestBody GoogleLoginRequest request, // @Valid needs the import
            HttpServletRequest servletRequest) {
        JwtResponse jwtResponse = authService.loginWithGoogle(request.getIdToken(), servletRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}