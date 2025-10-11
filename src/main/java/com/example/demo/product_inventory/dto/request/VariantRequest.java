package com.example.demo.product_inventory.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class VariantRequest {
    private String sku;
    private BigDecimal price;
    private String color;
}