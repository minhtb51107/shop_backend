package com.example.demo.supplychain.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.demo.user.entity.Employee;
import com.example.demo.user.repository.EmployeeRepository;

/**
 * Service để lấy thông tin user hiện tại
 * Trong thực tế, đây sẽ được tích hợp với Spring Security
 */
@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final EmployeeRepository employeeRepository;

    /**
     * Lấy Employee hiện tại đang đăng nhập
     * Tạm thời trả về employee có ID = 1
     * Trong thực tế sẽ lấy từ SecurityContext
     */
    public Employee getCurrentEmployee() {
        return employeeRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Current employee not found"));
    }

    /**
     * Lấy ID của employee hiện tại
     */
    public Integer getCurrentEmployeeId() {
        return getCurrentEmployee().getId();
    }
}
