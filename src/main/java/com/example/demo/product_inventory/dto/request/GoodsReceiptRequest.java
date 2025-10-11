// File: dto/request/GoodsReceiptRequest.java
package com.example.demo.product_inventory.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class GoodsReceiptRequest {
    private Long purchaseOrderId; // Optional
    private Integer warehouseId;
    private Integer createdByEmployeeId;
    private List<GoodsReceiptItemRequest> items;
}