package com.example.demo.sale.dto.response;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class ShipmentResponse {
    private Long id;
    private Long orderId;
    private String trackingCode;
    private String status;
    private OffsetDateTime shippedAt;
}