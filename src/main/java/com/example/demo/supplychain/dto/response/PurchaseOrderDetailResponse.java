package com.example.demo.supplychain.dto.response;

import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PurchaseOrderDetailResponse {
    private Integer id;
    private String supplierName;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private PurchaseOrderStatus status;
    private String createdByName;
    // Đã xóa List<ItemResponse> items và grandTotal
}