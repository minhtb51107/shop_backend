package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.response.OrderItemResponse;
import com.example.demo.sale.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderItemMapper {
    public OrderItemResponse toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        OrderItemResponse dto = new OrderItemResponse();
        dto.setId(orderItem.getId());
        dto.setVariantId(orderItem.getVariant().getId());
        dto.setVariantSku(orderItem.getVariant().getSku());
        // Giả sử có một trường 'name' trong Product
        dto.setVariantName(orderItem.getVariant().getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getPriceAtPurchase());
        dto.setTotalPrice(orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        return dto;
    }
}