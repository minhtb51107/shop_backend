package com.example.demo.sale.dto.response;

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
public class PromotionResponse {
    private Integer id;
    private String name;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String discountType;
    private BigDecimal discountValue;
    private Boolean isActive;
}