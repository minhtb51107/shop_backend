package com.example.demo.supplychain.controller;

import com.example.demo.supplychain.dto.request.CreatePurchaseOrderRequest;
import com.example.demo.supplychain.dto.response.PurchaseOrderDetailResponse;
import com.example.demo.supplychain.dto.response.PurchaseOrderSummaryResponse;
import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import com.example.demo.supplychain.service.PurchaseOrderService;
import jakarta.validation.Valid; // Thêm import
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Thêm import
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PURCHASING_MANAGER')") // Phân quyền: Ai được tạo đơn
    public ResponseEntity<PurchaseOrderDetailResponse> createPurchaseOrder(@Valid @RequestBody CreatePurchaseOrderRequest request) {
        PurchaseOrderDetailResponse createdOrder = purchaseOrderService.createPurchaseOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PURCHASING_MANAGER')")
    public ResponseEntity<PurchaseOrderDetailResponse> getPurchaseOrderById(@PathVariable Integer id) {
        PurchaseOrderDetailResponse orderDetail = purchaseOrderService.findPurchaseOrderById(id);
        return ResponseEntity.ok(orderDetail);
    }

 // minhtb51107/shop_backend/shop_backend-integration/src/main/java/com/example/demo/supplychain/controller/PurchaseOrderController.java

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PURCHASING_MANAGER')")
    public ResponseEntity<Page<PurchaseOrderSummaryResponse>> getAllPurchaseOrders(
            @RequestParam(required = false) PurchaseOrderStatus status,
            Pageable pageable) { // Sửa ở đây

        Page<PurchaseOrderSummaryResponse> ordersPage = purchaseOrderService.findAllPurchaseOrders(pageable, status);
        return ResponseEntity.ok(ordersPage);
    }
    
    @PatchMapping("/{id}/approve") // Dùng PATCH vì chỉ cập nhật một phần
    @PreAuthorize("hasRole('ADMIN')") // Chỉ Admin được duyệt
    public ResponseEntity<PurchaseOrderDetailResponse> approvePurchaseOrder(@PathVariable Integer id) {
        PurchaseOrderDetailResponse updatedOrder = purchaseOrderService.approvePurchaseOrder(id);
        return ResponseEntity.ok(updatedOrder);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'PURCHASING_MANAGER')")
    public ResponseEntity<PurchaseOrderDetailResponse> cancelPurchaseOrder(@PathVariable Integer id) {
        PurchaseOrderDetailResponse updatedOrder = purchaseOrderService.cancelPurchaseOrder(id);
        return ResponseEntity.ok(updatedOrder);
    }
}