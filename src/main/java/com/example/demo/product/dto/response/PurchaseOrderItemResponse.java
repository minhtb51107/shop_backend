// File: dto/response/PurchaseOrderItemResponse.java
package com.example.demo.product.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseOrderItemResponse {
    private Long id;
    private Long purchaseOrderId;
    private Long variantId;
    private String variantSku;
    private Integer quantity;
    private BigDecimal unitPrice;
}