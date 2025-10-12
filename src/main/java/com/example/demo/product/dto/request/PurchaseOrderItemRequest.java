// File: dto/request/PurchaseOrderItemRequest.java
package com.example.demo.product.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseOrderItemRequest {
    private Long variantId;
    private Integer quantity;
    private BigDecimal unitPrice;
}