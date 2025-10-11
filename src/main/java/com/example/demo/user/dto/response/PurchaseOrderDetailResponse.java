package com.example.demo.user.dto.response;

import com.example.demo.user.enums.PurchaseOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseOrderDetailResponse {
    private Long id;
    private String supplierName;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private PurchaseOrderStatus status;
    private String createdByName;
    private List<ItemResponse> items;
    private BigDecimal grandTotal; // Tổng tiền của đơn hàng

    @Data
    public static class ItemResponse {
        private Long itemId;
        private Long variantId;
        private String variantSku; // Mã SKU của biến thể sản phẩm
        private String variantName; // Tên của biến thể sản phẩm
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice; // Thành tiền (số lượng * đơn giá)
    }
}