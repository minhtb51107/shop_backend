package com.example.demo.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockRequest {

    @NotNull(message = "Variant ID is required")
    private Long variantId;

    @NotNull(message = "Warehouse ID is required")
    private Integer warehouseId; // Giữ kiểu Integer khớp với InventoryId

    @NotNull(message = "Quantity change is required")
    @Min(value = 1, message = "Quantity change must be at least 1 for SALE/OUT/IN/RETURN types. Use ADJUSTMENT for direct setting or negative changes.") // Ít nhất là 1 cho các giao dịch thông thường
    private Integer quantityChange; // Số lượng thay đổi (VD: bán 2 cái thì đây là 2)

    @NotBlank(message = "Transaction type is required")
    private String transactionType; // Ví dụ: "SALE", "IN", "RETURN", "ADJUSTMENT", "OUT"

    private String referenceId; // Tùy chọn: ID đơn hàng, phiếu nhập,...
    private String notes; // Tùy chọn: Ghi chú
}