package com.example.demo.sale.service;

import com.example.demo.sale.dto.response.OrderStatusHistoryResponse;
import java.util.List;

public interface OrderStatusHistoryService {
    List<OrderStatusHistoryResponse> getHistoryByOrderId(Long orderId);
    // Có thể thêm các phương thức khác tùy thuộc vào nghiệp vụ
}