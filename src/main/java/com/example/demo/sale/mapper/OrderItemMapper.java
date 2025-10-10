package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.response.OrderItemResponse;
import com.example.demo.sale.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItemResponse toDto(OrderItem orderItem);
}