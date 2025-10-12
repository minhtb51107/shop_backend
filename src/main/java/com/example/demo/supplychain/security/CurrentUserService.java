package com.example.demo.supplychain.security;

import com.example.demo.shared.exception.ResourceNotFoundException;
import com.example.demo.user.entity.Employee;
import com.example.demo.user.repository.EmployeeRepository;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Lấy Employee hiện tại đang đăng nhập từ SecurityContext.
     */
    public Employee getCurrentEmployee() {
        // 1. Lấy đối tượng Authentication từ context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Không có người dùng nào được xác thực.");
        }

        // 2. Lấy email (chính là username) từ đối tượng Authentication
        String email = authentication.getName();

        // 3. Tìm Employee thông qua User liên kết với email
        return userRepository.findByEmail(email)
                .map(user -> employeeRepository.findByUser_Id(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin nhân viên cho người dùng: " + email)))
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));
    }

    /**
     * Lấy ID của employee hiện tại.
     */
    public Integer getCurrentEmployeeId() {
        return getCurrentEmployee().getId();
    }
}