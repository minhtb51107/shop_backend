package com.example.demo.config;

import com.example.demo.config.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // <<< THÊM IMPORT NÀY
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Giữ lại để dùng @PreAuthorize ở Controller nếu cần
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    // private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler; // Nếu có dùng

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // --- Các endpoint xác thực/public cơ bản ---
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/auth/register",
                                "/api/v1/auth/google", // Endpoint nhận token từ Google FE
                                "/api/v1/auth/refresh-token",
                                "/api/v1/auth/forgot-password",
                                "/api/v1/auth/reset-password",
                                "/api/v1/auth/activate"
                                // "/api/v1/auth/me" // Tạm thời bỏ khỏi permitAll, để nó yêu cầu authenticated
                        ).permitAll()

                        // --- Cho phép xem sản phẩm, danh mục, brand (METHOD GET) ---
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/products/**",     // Xem list và chi tiết sản phẩm
                                "/api/v1/categories/**", // Xem list và chi tiết danh mục
                                "/api/v1/brands/**"      // Xem list và chi tiết thương hiệu
                                // Thêm các API GET public khác nếu cần (vd: xem khuyến mãi public)
                        ).permitAll()

                        // --- Swagger UI ---
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                         // --- Endpoint lấy thông tin user hiện tại ---
                        .requestMatchers("/api/v1/auth/me").authenticated() // Yêu cầu đăng nhập

                         // --- Các API cần đăng nhập khác (ví dụ: đặt hàng, xem đơn hàng cá nhân) ---
                        .requestMatchers("/api/v1/orders/**").authenticated()
                        .requestMatchers("/api/v1/cart/**").authenticated() // Nếu có API giỏ hàng phía server
                        .requestMatchers("/api/v1/customers/me").authenticated() // API cập nhật profile
                        
                        // --- Các API quản trị (có thể dùng @PreAuthorize ở Controller hoặc config cụ thể ở đây) ---
                        // Ví dụ: .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // --- Bất kỳ request nào khác chưa được định nghĩa ở trên đều yêu cầu xác thực ---
                        .anyRequest().authenticated()
                )
                // .oauth2Login(oauth2 -> oauth2 // Cấu hình OAuth2 nếu backend xử lý redirect từ Google
                //     .loginPage("/login") // Trang login của bạn nếu cần
                //     .successHandler(oAuth2LoginSuccessHandler)
                // )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .logout(logout -> logout.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}