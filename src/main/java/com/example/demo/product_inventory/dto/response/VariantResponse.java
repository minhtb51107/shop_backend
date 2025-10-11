package com.example.demo.product_inventory.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class VariantResponse {
    private Long id;
    private String sku;
    private BigDecimal price;
    private String color;

    // Thông tin từ sản phẩm cha để frontend dễ hiển thị
    private Integer productId;
    private String productName;
}