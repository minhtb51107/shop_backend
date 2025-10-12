package com.example.demo.sale.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {
    private Long orderId;
    private BigDecimal amount;
    private String method;
}