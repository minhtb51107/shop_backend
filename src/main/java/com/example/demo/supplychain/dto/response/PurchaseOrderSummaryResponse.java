package com.example.demo.supplychain.dto.response;

import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PurchaseOrderSummaryResponse {
    private Integer id;
    private String supplierName;
    private LocalDate orderDate;
    private PurchaseOrderStatus status;
    // Đã xóa grandTotal
}