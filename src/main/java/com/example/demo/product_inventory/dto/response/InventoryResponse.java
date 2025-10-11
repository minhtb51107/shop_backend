package com.example.demo.product_inventory.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class InventoryResponse {
    // ---- Từ Khóa chính ----
    private Long variantId;
    private Integer warehouseId;

    // ---- Dữ liệu chính ----
    private Integer stockQuantity;

    // ---- Thông tin bổ sung để hiển thị ----
    private String variantSku;
    private BigDecimal variantPrice;
    private String warehouseName;
    private String warehouseLocation;
}