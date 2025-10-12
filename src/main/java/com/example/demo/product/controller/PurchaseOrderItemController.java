package com.example.demo.product.controller;

import com.example.demo.product.dto.request.PurchaseOrderItemRequest;
import com.example.demo.product.dto.response.PurchaseOrderItemResponse;
import com.example.demo.product.service.PurchaseOrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-orders") // Base path là của "cha"
public class PurchaseOrderItemController {

    private final PurchaseOrderItemService itemService;

    public PurchaseOrderItemController(PurchaseOrderItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<PurchaseOrderItemResponse> addItemToOrder(
            @PathVariable Long orderId,
            @RequestBody PurchaseOrderItemRequest request) {
        PurchaseOrderItemResponse response = itemService.addItemToPurchaseOrder(orderId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}