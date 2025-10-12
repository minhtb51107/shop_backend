package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreateOrderRequest;
import com.example.demo.sale.dto.response.OrderItemResponse;
import com.example.demo.sale.dto.response.OrderResponse;
import com.example.demo.sale.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    @Autowired
    private OrderItemMapper orderItemMapper;

    public Order toEntity(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }

        Order order = new Order();
        order.setShippingAddress(request.getShippingAddress());
        return order;
    }

    public OrderResponse toDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId().longValue());
        }
        dto.setShippingAddress(order.getShippingAddress());
        if (order.getWarehouse() != null) {
            dto.setWarehouseId(order.getWarehouse().getId().longValue());
            dto.setWarehouseName(order.getWarehouse().getName());
        }
        dto.setGrandTotal(order.getGrandTotal());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        if (order.getHandledBy() != null) {
            dto.setHandledByEmployeeId(order.getHandledBy().getId());
            dto.setHandledByEmployeeName(order.getHandledBy().getFullname());
        }
        if (order.getItems() != null) {
            List<OrderItemResponse> itemResponses = order.getItems().stream()
                    .map(orderItemMapper::toDto)
                    .collect(Collectors.toList());
            dto.setItems(itemResponses);
        }

        return dto;
    }

    public List<OrderResponse> toDtoList(List<Order> orders) {
        if (orders == null) {
            return null;
        }
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}