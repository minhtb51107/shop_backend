package com.example.demo.sale.dto.response;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class OrderStatusHistoryResponse {
    private Long id;
    private Long orderId;
    private String status;
    private String notes;
    private OffsetDateTime createdAt;
    private Integer updatedByEmployeeId;
}