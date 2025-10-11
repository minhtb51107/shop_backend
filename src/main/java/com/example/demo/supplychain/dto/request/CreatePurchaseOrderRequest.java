package com.example.demo.supplychain.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreatePurchaseOrderRequest {
    private Integer supplierId;
    private LocalDate expectedDeliveryDate;
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private Long variantId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}