package com.example.demo.user.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VariantRequest {
    private Integer productId;
    private String sku;
    private BigDecimal price;
    private String color;
}
