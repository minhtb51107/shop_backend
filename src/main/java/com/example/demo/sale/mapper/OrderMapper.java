package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreateOrderRequest;
import com.example.demo.sale.dto.response.OrderResponse;
import com.example.demo.sale.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "handledBy", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "statusHistories", ignore = true)
    @Mapping(target = "appliedPromotions", ignore = true)
    @Mapping(target = "shipments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "grandTotal", ignore = true)
    Order toEntity(CreateOrderRequest request);

    @Mapping(source = "customer.id", target = "customerId")
    OrderResponse toDto(Order order);
}