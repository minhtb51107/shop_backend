package com.example.demo.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.demo.user.dto.response.RoleWithPermissionsResponse;
import com.example.demo.user.entity.Permission;
import com.example.demo.user.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    // MapStruct sẽ tự động tìm và sử dụng phương thức toPermissionResponse bên dưới
    RoleWithPermissionsResponse toRoleWithPermissionsResponse(Role role);

    // Chuyển Permission Entity sang PermissionResponse DTO (lớp con trong DTO chính)
    RoleWithPermissionsResponse.PermissionResponse toPermissionResponse(Permission permission);
}