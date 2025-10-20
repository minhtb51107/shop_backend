package com.example.demo.config.security;

import com.example.demo.shared.util.JwtUtil; // Đảm bảo import đúng
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String userEmail = null; // Khởi tạo null

        // --- BƯỚC 1: Kiểm tra Header và Trích xuất Token ---
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.trace("[doFilterInternal] No JWT token in header for {}", request.getRequestURI());
            filterChain.doFilter(request, response); // Không có token -> đi tiếp
            return;
        }
        jwt = authHeader.substring(7);
        log.trace("[doFilterInternal] Found JWT: {}", jwt); // Cẩn thận khi log token ở production

        // --- BƯỚC 2: Xác thực chữ ký/thời hạn và trích xuất Email ---
        // Sử dụng validateToken trước để loại bỏ token lỗi/hết hạn
        if (!jwtUtil.validateToken(jwt)) {
             log.warn("[doFilterInternal] JWT validation failed (expired, malformed, etc.). Token: {}", jwt);
             SecurityContextHolder.clearContext(); // Xóa context nếu token không hợp lệ
             filterChain.doFilter(request, response); // Đi tiếp để các filter khác (hoặc config) trả về 401
             return; // Dừng xử lý JWT ở đây
        }

        // Nếu validateToken thành công, có thể lấy email (trừ khi hết hạn ngay tức thì)
        try {
            userEmail = jwtUtil.getEmailFromToken(jwt); // Sử dụng tên phương thức đúng
            log.debug("[doFilterInternal] Extracted email from valid JWT: {}", userEmail);
        } catch (ExpiredJwtException e) {
            // validateToken nên bắt được lỗi này, nhưng xử lý phòng ngừa
            log.warn("[doFilterInternal] JWT expired after initial validation? Email: {}. Token: {}", userEmail, jwt, e);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        } catch (JwtException e) {
            log.warn("[doFilterInternal] Error extracting email after initial validation? Email: {}. Token: {}", userEmail, jwt, e);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) { // Bắt các lỗi không mong muốn khác
            log.error("[doFilterInternal] Unexpected error extracting email after validation. Email: {}. Token: {}", userEmail, jwt, e);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }


        // --- BƯỚC 3: Load UserDetails và Thiết lập Authentication nếu cần ---
        // Chỉ thực hiện nếu lấy được email VÀ chưa có ai đăng nhập HOẶC đang là anonymous
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        boolean shouldAuthenticate = (currentAuth == null || currentAuth instanceof AnonymousAuthenticationToken || !currentAuth.isAuthenticated());

        if (userEmail != null && shouldAuthenticate) {
            log.debug("[doFilterInternal] Attempting UserDetails load for: {}", userEmail);
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // *** QUAN TRỌNG: Kiểm tra email từ token có khớp với UserDetails không ***
                if (userEmail.equals(userDetails.getUsername())) {
                    log.debug("[doFilterInternal] UserDetails loaded and email matches. Creating Authentication token.");
                    // Tạo đối tượng Authentication
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Credentials không cần thiết với JWT
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    // Cập nhật SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("[doFilterInternal] Successfully authenticated user '{}' from JWT.", userEmail);
                } else {
                    // Trường hợp này không nên xảy ra nếu logic getEmailFromToken đúng
                    log.error("[doFilterInternal] Mismatch! Token email ('{}') != UserDetails username ('{}'). Clearing context.", userEmail, userDetails.getUsername());
                    SecurityContextHolder.clearContext();
                }
            } catch (UsernameNotFoundException e) {
                log.warn("[doFilterInternal] User '{}' from JWT not found in database. Clearing context.", userEmail);
                SecurityContextHolder.clearContext(); // User không tồn tại -> không xác thực
            } catch (Exception e) {
                log.error("[doFilterInternal] Error during UserDetails loading or auth creation for '{}'. Clearing context.", userEmail, e);
                SecurityContextHolder.clearContext(); // Lỗi khác -> không xác thực
            }
        } else if (userEmail != null) {
            // Đã có authentication hợp lệ khác (không phải anonymous), không ghi đè
             log.trace("[doFilterInternal] Security context already holds valid authentication for '{}'. Skipping JWT auth.", userEmail);
        }
        // else: userEmail là null (đã được xử lý ở BƯỚC 2)

        // --- BƯỚC 4: Luôn gọi filter tiếp theo ---
        filterChain.doFilter(request, response);
    }
}