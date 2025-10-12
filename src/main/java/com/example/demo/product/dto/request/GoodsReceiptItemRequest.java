// File: dto/request/GoodsReceiptItemRequest.java
package com.example.demo.product.dto.request;


import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class GoodsReceiptItemRequest {
    private Long variantId;
    private Integer quantityReceived;
    private BigDecimal unitCost;
    private Long purchaseOrderItemId; // Optional
}