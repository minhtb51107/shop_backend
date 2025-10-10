package com.example.demo.user.dto.request;

import lombok.Data;

@Data
public class InventoryRequest {
    private Long variantId;
    private Integer warehouseId;
    private Integer stockQuantity;
}
