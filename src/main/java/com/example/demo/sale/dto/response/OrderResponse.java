package com.example.demo.sale.dto.response;

import com.example.demo.sale.enums.OrderStatus; // Import Enum nếu muốn dùng Enum thay vì String
import lombok.Data;
import java.math.BigDecimal;
// import java.time.OffsetDateTime; // Bỏ OffsetDateTime
import java.time.LocalDateTime; // Dùng LocalDateTime
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long customerId;
    // Optional: Thêm tên khách hàng nếu cần
    // private String customerName;

    // Các trường mới khớp với Order entity và CreateOrderRequest
    private String shippingAddress;
    private String receiverFullname;
    private String receiverPhoneNumber;
    private String notes;
    private String shippingMethod;
    private String paymentMethod;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount; // Thay cho grandTotal

    // Trạng thái (có thể dùng String hoặc Enum OrderStatus)
    // private String status;
     private OrderStatus status; // Dùng Enum cho chặt chẽ hơn

    // Ngày tháng (dùng LocalDateTime)
    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Bỏ các trường không còn dùng
    // private Long warehouseId;
    // private String warehouseName;
    // private Integer handledByEmployeeId;
    // private String handledByEmployeeName;

    // Danh sách item giữ nguyên
    private List<OrderItemResponse> items;
}