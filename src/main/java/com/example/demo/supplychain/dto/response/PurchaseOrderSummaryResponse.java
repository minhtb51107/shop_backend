package com.example.demo.supplychain.dto.response;

import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurchaseOrderSummaryResponse {
    private Integer id; // Đổi từ Long sang Integer để consistency
    private String supplierName;
    private LocalDate orderDate;
    private PurchaseOrderStatus status;
    private BigDecimal grandTotal;
}