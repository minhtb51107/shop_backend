package com.example.demo.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.user.dto.request.CreateEmployeeRequest;
import com.example.demo.user.dto.request.UpdateEmployeeRequest;
import com.example.demo.user.dto.response.EmployeeResponse;

public interface EmployeeService {

    /**
     * Tạo mới một nhân viên.
     * @param request DTO chứa thông tin nhân viên mới.
     * @return DTO chứa thông tin nhân viên vừa tạo.
     */
    EmployeeResponse createEmployee(CreateEmployeeRequest request);
    
    EmployeeResponse getEmployeeById(Integer id);
    
    Page<EmployeeResponse> getAllEmployees(Pageable pageable);
    
    EmployeeResponse updateEmployee(Integer id, UpdateEmployeeRequest request);
    
    void updateEmployeeStatus(Integer id, boolean isActive);
}
