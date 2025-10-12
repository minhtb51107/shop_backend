package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreateOrderRequest;
import com.example.demo.sale.dto.response.OrderResponse;
import com.example.demo.sale.entity.Order;
import com.example.demo.sale.entity.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "handledBy", ignore = true)
    @Mapping(target = "statusHistories", ignore = true)
    @Mapping(target = "appliedPromotions", ignore = true)
    @Mapping(target = "shipments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "grandTotal", ignore = true)
    Order toEntity(CreateOrderRequest request);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(target = "grandTotal", ignore = true)
    OrderResponse toDto(Order order);
    
    List<OrderResponse> toDtoList(List<Order> orders);

    @AfterMapping
    default void calculateGrandTotalForDto(@MappingTarget OrderResponse dto, Order order) {
        if (order.getItems() != null) {
            BigDecimal grandTotal = order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setGrandTotal(grandTotal);
        }
    }
}