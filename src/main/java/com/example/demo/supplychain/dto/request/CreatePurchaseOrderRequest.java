package com.example.demo.supplychain.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreatePurchaseOrderRequest {
    private Integer supplierId;
    private LocalDate expectedDeliveryDate;
    // Đã xóa List<ItemRequest> items;
}