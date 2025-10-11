package com.example.demo.product_inventory.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String skuPrefix;
    private String description;
    private Boolean isActive;

    private Integer brandId;
    private Integer categoryId;

    // Dùng Map để linh hoạt nhận thông số cho cả Laptop, Phone,...
    // Ví dụ: { "cpu": "Intel i7", "ram": "16GB" }
    private Map<String, String> specs;
}