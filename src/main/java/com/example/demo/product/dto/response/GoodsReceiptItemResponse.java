// File: dto/response/GoodsReceiptItemResponse.java
package com.example.demo.product.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class GoodsReceiptItemResponse {
    private Long id;
    private Long goodsReceiptId;
    private Long variantId;
    private String variantSku;
    private Integer quantityReceived;
    private BigDecimal unitCost;
}