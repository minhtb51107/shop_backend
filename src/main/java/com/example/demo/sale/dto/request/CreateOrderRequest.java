package com.example.demo.sale.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class CreateOrderRequest {
    private Long customerId;
    private Long warehouseId;
    private String shippingAddress;
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private Long variantId;
        private Integer quantity;
    }
}