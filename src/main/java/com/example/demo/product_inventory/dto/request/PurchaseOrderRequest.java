// File: dto/request/PurchaseOrderRequest.java
package com.example.demo.product_inventory.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderRequest {
    private Integer supplierId;
    private LocalDate expectedDeliveryDate;
    private Integer createdByEmployeeId;
    private List<PurchaseOrderItemRequest> items;
}