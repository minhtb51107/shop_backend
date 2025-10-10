package com.example.demo.sale.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long customerId;
    private String shippingAddress;
    private BigDecimal grandTotal;
    private String status;
    private OffsetDateTime createdAt;
    private List<OrderItemResponse> items;
}