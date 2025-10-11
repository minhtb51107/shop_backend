package com.example.demo.supplychain.dto.response;

import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseOrderDetailResponse {
    private Integer id; // Đổi từ Long sang Integer để consistency
    private String supplierName;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private PurchaseOrderStatus status;
    private String createdByName;
    private List<ItemResponse> items;
    private BigDecimal grandTotal; // Tổng tiền của đơn hàng

    @Data
    public static class ItemResponse {
        private Integer itemId; // Đổi từ Long sang Integer để consistency
        private Integer variantId; // Đổi từ Long sang Integer để consistency
        private String variantSku; // Mã SKU của biến thể sản phẩm
        private String variantName; // Tên của biến thể sản phẩm
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice; // Thành tiền (số lượng * đơn giá)
    }
}