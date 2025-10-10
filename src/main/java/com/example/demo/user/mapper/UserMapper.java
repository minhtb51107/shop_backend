package com.example.demo.user.mapper;

import com.example.demo.user.dto.response.UserDetailsResponse;
import com.example.demo.user.entity.Employee;
import com.example.demo.user.entity.Permission;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Bỏ qua các trường sẽ được xử lý trong @AfterMapping để hết warning
    @org.mapstruct.Mapping(target = "fullname", ignore = true)
    @org.mapstruct.Mapping(target = "userType", ignore = true)
    @org.mapstruct.Mapping(target = "roles", ignore = true)
    @org.mapstruct.Mapping(target = "permissions", ignore = true)
    UserDetailsResponse toUserDetailsResponse(User user);

    @AfterMapping
    default void afterMapToUserDetailsResponse(User user, @MappingTarget UserDetailsResponse response) {
        if (user.getCustomer() != null) {
            response.setFullname(user.getCustomer().getFullname());
            response.setUserType("CUSTOMER");
        } else if (user.getEmployee() != null) {
            Employee employee = user.getEmployee();
            response.setFullname(employee.getFullname());
            response.setUserType("EMPLOYEE");
            
            if (employee.getRoles() != null) {
                response.setRoles(employee.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()));
                
                response.setPermissions(employee.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(Permission::getName)
                        .collect(Collectors.toSet()));
            }
        }
    }
}