package com.example.demo.product_inventory.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockRequest {
    private Integer stockQuantity;
}