package com.example.demo.user.dto.response;

import lombok.Data;

@Data
public class InventoryResponse {
    private Long variantId;
    private Integer warehouseId;
    private Integer stockQuantity;
    private String productName;
    private String warehouseName;
}
