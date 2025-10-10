package com.example.demo.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.user.dto.response.UserActivityLogResponse;
import com.example.demo.user.entity.UserActivityLog;

@Mapper(componentModel = "spring")
public interface UserActivityLogMapper {
    @Mapping(source = "user.email", target = "userEmail")
    UserActivityLogResponse toResponse(UserActivityLog log);
}