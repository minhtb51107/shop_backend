package com.example.demo.finance.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class OrderSummaryResponse {
    private Long orderId;
    private OffsetDateTime transactionDate;
    private String description;
    private BigDecimal amount;
}