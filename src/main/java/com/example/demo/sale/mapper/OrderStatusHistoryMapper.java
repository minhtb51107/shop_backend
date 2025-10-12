package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.response.OrderStatusHistoryResponse;
import com.example.demo.sale.entity.OrderStatusHistory;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderStatusHistoryMapper {
    public OrderStatusHistoryResponse toDto(OrderStatusHistory statusHistory) {
        if (statusHistory == null) {
            return null;
        }
        OrderStatusHistoryResponse dto = new OrderStatusHistoryResponse();
        dto.setId(statusHistory.getId());
        dto.setOrderId(statusHistory.getOrder().getId());
        dto.setStatus(statusHistory.getStatus());
        dto.setNotes(statusHistory.getNotes());
        dto.setCreatedAt(statusHistory.getCreatedAt());
        if (statusHistory.getUpdatedBy() != null) {
            dto.setUpdatedByEmployeeId(statusHistory.getUpdatedBy().getId());
        }
        return dto;
    }

    public List<OrderStatusHistoryResponse> toDtoList(List<OrderStatusHistory> histories) {
        return histories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}