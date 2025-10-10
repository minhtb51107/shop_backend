package com.example.demo.user.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private Integer brandId;
    private Integer categoryId;
    private String name;
    private String skuPrefix;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
}
