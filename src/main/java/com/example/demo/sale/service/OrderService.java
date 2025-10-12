package com.example.demo.sale.service;

import com.example.demo.sale.dto.request.CreateOrderRequest;
import com.example.demo.sale.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse getOrderById(Long orderId);
    void updateOrderStatus(Long orderId, String newStatus);
    List<OrderResponse> findAllOrders();
}