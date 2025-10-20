package com.example.demo.sale.service;

import com.example.demo.sale.dto.request.CreateOrderRequest;
import com.example.demo.sale.dto.response.OrderResponse;
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.Pageable; // Import Pageable

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse getOrderById(Long orderId);
    void updateOrderStatus(Long orderId, String newStatus);
    List<OrderResponse> findAllOrders(); // Giữ lại nếu cần cho admin

    // --- THÊM PHƯƠNG THỨC MỚI ---
    Page<OrderResponse> getMyOrders(Pageable pageable);
}