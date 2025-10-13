// src/main/java/com/example/demo/product/controller/PurchaseOrderItemController.java
package com.example.demo.product.controller;

import com.example.demo.product.dto.request.PurchaseOrderItemRequest;
import com.example.demo.product.dto.response.PurchaseOrderItemResponse;
import com.example.demo.product.service.PurchaseOrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // THÊM IMPORT
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-orders") // GIỮ NGUYÊN BASE PATH
@PreAuthorize("hasAnyRole('ADMIN', 'PURCHASING_MANAGER')") // Bảo vệ toàn bộ controller
public class PurchaseOrderItemController {

    private final PurchaseOrderItemService itemService;

    public PurchaseOrderItemController(PurchaseOrderItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<PurchaseOrderItemResponse> addItemToOrder(
            @PathVariable Integer orderId,
            @RequestBody PurchaseOrderItemRequest request) {
        PurchaseOrderItemResponse response = itemService.addItemToPurchaseOrder(orderId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // *** THÊM ENDPOINT UPDATE ***
    @PutMapping("/items/{itemId}")
    public ResponseEntity<PurchaseOrderItemResponse> updateItem(
            @PathVariable Long itemId,
            @RequestBody PurchaseOrderItemRequest request) {
        PurchaseOrderItemResponse response = itemService.updateItem(itemId, request);
        return ResponseEntity.ok(response);
    }
    
    // *** THÊM ENDPOINT DELETE ***
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}