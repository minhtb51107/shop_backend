// File: controller/GoodsReceiptController.java
package com.example.demo.product_inventory.controller;

import com.example.demo.product_inventory.dto.request.GoodsReceiptRequest;
import com.example.demo.product_inventory.dto.response.GoodsReceiptResponse;
import com.example.demo.product_inventory.service.GoodsReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goods-receipts")
public class GoodsReceiptController {

    private final GoodsReceiptService grService;

    public GoodsReceiptController(GoodsReceiptService grService) {
        this.grService = grService;
    }

    @PostMapping
    public ResponseEntity<GoodsReceiptResponse> createGoodsReceipt(@RequestBody GoodsReceiptRequest request) {
        GoodsReceiptResponse response = grService.createGoodsReceipt(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodsReceiptResponse> getGoodsReceiptById(@PathVariable Long id) {
        return grService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}