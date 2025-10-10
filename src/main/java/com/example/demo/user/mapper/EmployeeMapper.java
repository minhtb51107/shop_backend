package com.example.demo.user.mapper;

import com.example.demo.user.dto.request.CreateEmployeeRequest;
import com.example.demo.user.dto.response.EmployeeResponse;
import com.example.demo.user.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Employee toEmployeeEntity(CreateEmployeeRequest request);
    
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.status", target = "status")
    @Mapping(source = "active", target = "isActive")
    // SỬA LẠI EXPRESSION, GHI ĐẦY ĐỦ ĐƯỜNG DẪN PACKAGE
    @Mapping(target = "roleNames", expression = "java(employee.getRoles() != null ? employee.getRoles().stream().map(com.example.demo.user.entity.Role::getName).collect(java.util.stream.Collectors.toSet()) : java.util.Collections.emptySet())")
    EmployeeResponse toEmployeeResponse(Employee employee);
}