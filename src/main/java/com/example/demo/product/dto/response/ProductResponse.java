package com.example.demo.product.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class ProductResponse {
    private Integer id;
    private String name;
    private String skuPrefix;
    private String description;
    private Boolean isActive;

    private BrandResponse brand;
    private ProductCategoryResponse category;

    // Dùng Map để trả về thông số kỹ thuật
    private Map<String, String> specs;
}