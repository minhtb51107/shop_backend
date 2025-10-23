package com.example.demo.sale.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderRequest {

    // customerId sẽ được lấy từ user đang đăng nhập, không cần gửi từ frontend nữa
    // private Long customerId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId; // Đổi từ Integer sang Long cho nhất quán

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Receiver fullname is required")
    private String receiverFullname;

    @NotBlank(message = "Receiver phone number is required")
    private String receiverPhoneNumber;

    private String notes; // Ghi chú

    @NotBlank(message = "Shipping method is required")
    private String shippingMethod; // Ví dụ: "standard", "express"

    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // Ví dụ: "cod", "bank_transfer"

    // Tùy chọn: Thêm nếu frontend gửi lên và muốn lưu/xử lý
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid // Đảm bảo các ItemRequest bên trong cũng được validate
    private List<OrderItemRequest> items; // Đổi tên class con thành OrderItemRequest cho rõ ràng

    @Data
    public static class OrderItemRequest { // Đổi tên class con
        @NotNull(message = "Variant ID is required")
        private Long variantId;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        private Integer quantity;

        @NotNull(message = "Unit price is required")
        @Positive(message = "Unit price must be positive")
        private BigDecimal unitPrice; // Giá này frontend gửi lên, có thể dùng hoặc lấy giá mới nhất từ DB
    }
}