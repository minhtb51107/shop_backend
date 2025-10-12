package com.example.demo.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInventoryRequest {
    private Long variantId;
    private Integer warehouseId;
    private Integer initialStock;
}