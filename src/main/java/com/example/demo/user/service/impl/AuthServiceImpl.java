package com.example.demo.user.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import com.example.demo.shared.util.JwtUtil;
import com.example.demo.user.dto.request.LoginRequest;
import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.JwtResponse;
import com.example.demo.user.entity.Customer;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserStatus;
import com.example.demo.user.mapper.CustomerMapper;
import com.example.demo.user.repository.CustomerRepository;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.AuthService;
import com.example.demo.user.entity.UserSession;
import com.example.demo.user.repository.UserSessionRepository;
import java.time.OffsetDateTime;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Tự động inject các dependency final
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserSessionRepository userSessionRepository;
    
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    
    @Value("${app.jwt.refresh-token-expiration-ms}") 
    private long refreshTokenExpirationMs;

    @Override
    public void registerCustomer(RegisterRequest request) {
        // 1. Validate dữ liệu
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email đã được sử dụng.");
        }
        if (customerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BadRequestException("Số điện thoại đã được sử dụng.");
        }

        // 2. Lấy Role mặc định cho khách hàng
        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò 'CUSTOMER' không tồn tại."));

        // 3. Tạo User và mã hóa mật khẩu
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        // 4. Tạo Customer và liên kết với User
        Customer customer = customerMapper.toCustomerEntity(request);
        customer.setUser(user);

        // 5. Lưu vào CSDL (do có cascade, user sẽ được lưu cùng)
        customerRepository.save(customer);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        // 1. Xác thực bằng Spring Security
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Nếu xác thực thành công, đặt vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Lấy thông tin user từ CSDL
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại."));

        // 4. Tạo access token và refresh token
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        
        // Lưu refresh token vào CSDL
        UserSession session = UserSession.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiresAt(OffsetDateTime.now().plusSeconds(refreshTokenExpirationMs / 1000))
                // TODO: Lấy User-Agent và IP Address từ request để tăng cường bảo mật
                .build();
        userSessionRepository.save(session);

        // TODO: Lưu refresh token vào bảng user_sessions

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    
    @Override
    public JwtResponse refreshToken(String refreshToken) {
        // 1. Tìm session trong CSDL
        UserSession session = userSessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Refresh token không hợp lệ."));

        // 2. Kiểm tra token đã hết hạn chưa
        if (session.getExpiresAt().isBefore(OffsetDateTime.now())) {
            userSessionRepository.delete(session); // Xóa token hết hạn
            throw new BadRequestException("Refresh token đã hết hạn.");
        }

        // 3. Lấy thông tin user
        User user = session.getUser();

        // 4. Tạo access token mới
        String newAccessToken = jwtUtil.generateAccessToken(user);

        // Trả về access token mới (refresh token giữ nguyên)
        return JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
    
    @Override
    public JwtResponse loginWithGoogle(String idTokenString) {
        try {
            // 1. Xác thực ID Token với Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new BadRequestException("Token Google không hợp lệ.");
            }

            // 2. Lấy thông tin người dùng từ token payload
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            // 3. Tìm người dùng trong CSDL, nếu không có thì tạo mới
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> registerNewUserFromGoogle(payload));
            
            // 4. Tạo JWT token của hệ thống và trả về
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            // TODO: Lưu refresh token vào user_sessions giống như luồng đăng nhập thường
            
            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            throw new BadRequestException("Xác thực Google thất bại: " + e.getMessage());
        }
    }

    private User registerNewUserFromGoogle(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");

        // Tạo User mới
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Mật khẩu ngẫu nhiên, không dùng
        newUser.setStatus(UserStatus.ACTIVE);

        // Tạo Customer tương ứng
        Customer newCustomer = new Customer();
        newCustomer.setFullname(name);
        newCustomer.setPhoto(pictureUrl);
        newCustomer.setUser(newUser); // Liên kết với user
        // Số điện thoại có thể để trống (NULL)

        customerRepository.save(newCustomer); // Lưu customer (user sẽ được lưu theo nhờ cascade)
        
        return newUser;
    }
}