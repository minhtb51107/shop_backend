// File: dto/response/GoodsReceiptResponse.java
package com.example.demo.product_inventory.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class GoodsReceiptResponse {
    private Long id;
    private Long purchaseOrderId;
    private Integer warehouseId;
    private String warehouseName;
    private LocalDate receiptDate;
    private Integer createdByEmployeeId;
    private List<GoodsReceiptItemResponse> items;
}