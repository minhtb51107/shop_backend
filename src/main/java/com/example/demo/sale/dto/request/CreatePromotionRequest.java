package com.example.demo.sale.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePromotionRequest {
    private String name;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String discountType;
    private BigDecimal discountValue;
    private Boolean isActive;
}