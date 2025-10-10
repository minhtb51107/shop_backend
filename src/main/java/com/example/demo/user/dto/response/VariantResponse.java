package com.example.demo.user.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VariantResponse {
    private Long id;
    private String sku;
    private BigDecimal price;
    private String color;
    private String productName;
}
