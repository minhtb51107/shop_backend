package com.example.demo.sale.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class CreatePromotionRequest {
    private String name;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String discountType;
    private BigDecimal discountValue;
}