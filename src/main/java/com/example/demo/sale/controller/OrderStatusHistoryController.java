package com.example.demo.sale.controller;

import com.example.demo.sale.dto.response.OrderStatusHistoryResponse;
import com.example.demo.sale.service.OrderStatusHistoryService; // Giả định có OrderStatusHistoryService
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order-status-histories")
@RequiredArgsConstructor
public class OrderStatusHistoryController {
    private final OrderStatusHistoryService statusHistoryService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderStatusHistoryResponse>> getHistoryByOrderId(@PathVariable Long orderId) {
        List<OrderStatusHistoryResponse> history = statusHistoryService.getHistoryByOrderId(orderId);
        return ResponseEntity.ok(history);
    }
}