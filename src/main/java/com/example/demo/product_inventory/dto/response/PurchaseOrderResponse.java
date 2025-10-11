// File: dto/response/PurchaseOrderResponse.java
package com.example.demo.product_inventory.dto.response;

import com.example.demo.product_inventory.entity.PurchaseOrder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderResponse {
    private Long id;
    private Integer supplierId;
    private String supplierName;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private PurchaseOrder.POStatus status;
    private Integer createdByEmployeeId;
    private List<PurchaseOrderItemResponse> items;
}