package com.example.demo.user.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private Integer id;
    private String name;
    private String skuPrefix;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    private String brandName;
    private String categoryName;
}
