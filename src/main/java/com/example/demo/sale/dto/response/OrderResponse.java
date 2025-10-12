package com.example.demo.sale.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long customerId;
    private String shippingAddress;
    private Long warehouseId;
    private String warehouseName;
    private BigDecimal grandTotal;
    private String status;
    private OffsetDateTime createdAt;
    private Integer handledByEmployeeId;
    private String handledByEmployeeName;
    private List<OrderItemResponse> items;
}