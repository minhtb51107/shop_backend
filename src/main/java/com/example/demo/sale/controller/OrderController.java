package com.example.demo.sale.controller;

import com.example.demo.sale.dto.request.CreateOrderRequest;
import com.example.demo.sale.dto.response.OrderResponse;
import com.example.demo.sale.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.Pageable; // Import Pageable
import org.springframework.security.access.prepost.PreAuthorize; // Import PreAuthorize

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    
    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()") // Chỉ cần đăng nhập là được
    public ResponseEntity<Page<OrderResponse>> getMyOrders(Pageable pageable) {
        Page<OrderResponse> myOrders = orderService.getMyOrders(pageable);
        return ResponseEntity.ok(myOrders);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        OrderResponse createdOrder = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}