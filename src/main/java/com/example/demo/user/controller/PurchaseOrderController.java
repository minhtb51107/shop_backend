// File: src/main/java/com/example/demo/user/controller/PurchaseOrderController.java
package com.example.demo.user.controller;

import com.example.demo.user.dto.request.CreatePurchaseOrderRequest;
import com.example.demo.user.dto.response.PurchaseOrderDetailResponse;
import com.example.demo.user.dto.response.PurchaseOrderSummaryResponse;
import com.example.demo.user.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    public ResponseEntity<PurchaseOrderDetailResponse> createPurchaseOrder(@RequestBody CreatePurchaseOrderRequest request) {
        PurchaseOrderDetailResponse createdOrder = purchaseOrderService.createPurchaseOrder(request);
        // Trả về DTO chi tiết của đơn hàng vừa tạo với status 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDetailResponse> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrderDetailResponse orderDetail = purchaseOrderService.findPurchaseOrderById(id);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderSummaryResponse>> getAllPurchaseOrders() {
        List<PurchaseOrderSummaryResponse> orders = purchaseOrderService.findAllPurchaseOrders();
        return ResponseEntity.ok(orders);
    }
}