package com.example.demo.sale.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class PromotionResponse {
    private Integer id;
    private String name;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String discountType;
    private BigDecimal discountValue;
    private Boolean isActive;
}