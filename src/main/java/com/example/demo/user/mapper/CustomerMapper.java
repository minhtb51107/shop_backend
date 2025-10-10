package com.example.demo.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.CustomerResponse;
import com.example.demo.user.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    // Chuyển từ RegisterRequest DTO sang Customer Entity
    // Lưu ý: các trường của User (email, password) sẽ được xử lý ở tầng Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "photo", ignore = true)
    Customer toCustomerEntity(RegisterRequest request);

    // Chuyển từ Customer Entity sang CustomerResponse DTO
    @Mapping(source = "user.email", target = "email") // Lấy email từ User liên kết
    @Mapping(source = "user.status", target = "status") // Lấy status từ User
    CustomerResponse toCustomerResponse(Customer customer);
}