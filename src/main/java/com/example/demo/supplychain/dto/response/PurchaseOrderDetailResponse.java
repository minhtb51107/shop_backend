// src/main/java/com/example/demo/supplychain/dto/response/PurchaseOrderDetailResponse.java
package com.example.demo.supplychain.dto.response;

import com.example.demo.product.dto.response.PurchaseOrderItemResponse; // THÊM IMPORT NÀY
import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import lombok.Data;
import java.time.LocalDate;
import java.util.List; // THÊM IMPORT NÀY

@Data
public class PurchaseOrderDetailResponse {
    private Integer id;
    private String supplierName;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private PurchaseOrderStatus status;
    private String createdByName;
    
    // *** THÊM DÒNG NÀY ***
    private List<PurchaseOrderItemResponse> items;
}