package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.response.OrderStatusHistoryResponse;
import com.example.demo.sale.entity.OrderStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderStatusHistoryMapper {
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "updatedBy.id", target = "updatedByEmployeeId")
    OrderStatusHistoryResponse toDto(OrderStatusHistory statusHistory);
}