package com.example.demo.sale.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String method;
    private String status;
    private String transactionCode;
}