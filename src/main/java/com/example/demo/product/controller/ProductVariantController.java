// src/main/java/com/example/demo/product/controller/ProductVariantController.java
package com.example.demo.product.controller;

import com.example.demo.product.dto.request.VariantRequest;
import com.example.demo.product.dto.response.VariantResponse;
import com.example.demo.product.service.VariantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products") // *** Đã sửa base path ở lần trước ***
@RequiredArgsConstructor
@Slf4j
public class ProductVariantController {

    private final VariantService variantService;

    /**
     * Lấy tất cả các biến thể của một sản phẩm cụ thể.
     */
    @GetMapping("/{productId}/variants")
    public ResponseEntity<List<VariantResponse>> getVariantsForProduct(
            @PathVariable Integer productId // <<< SỬ DỤNG LẠI INTEGER
    ) {
        log.info("Request received: GET /api/v1/products/{}/variants", productId);
        try {
            List<VariantResponse> variants = variantService.getVariantsForProduct(productId); // Gọi service với Integer
            log.info("Found {} variants for product ID {}", variants.size(), productId);
            return ResponseEntity.ok(variants.isEmpty() ? Collections.emptyList() : variants);
        } catch (Exception e) {
            log.error("Error fetching variants for product ID {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tạo một biến thể mới cho một sản phẩm cụ thể.
     */
    @PostMapping("/{productId}/variants")
    public ResponseEntity<VariantResponse> createVariantForProduct(
            @PathVariable Integer productId, // <<< SỬ DỤNG LẠI INTEGER
            @RequestBody VariantRequest request) {
        log.info("Request received: POST /api/v1/products/{}/variants", productId);
        try {
            VariantResponse newVariant = variantService.createVariantForProduct(productId, request); // Gọi service với Integer
            return new ResponseEntity<>(newVariant, HttpStatus.CREATED);
       } catch (Exception e) {
            log.error("Error creating variant for product ID {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
    }

    // --- Các endpoint /variants/{id} ---
    // Vẫn khuyến nghị chuyển sang controller riêng (ví dụ: VariantController @RequestMapping("/api/v1/variants"))
    // Nếu giữ lại ở đây, base path của class cần đổi thành @RequestMapping("/api/v1")
    // và các mapping ở trên cần sửa thành @GetMapping("/products/{productId}/variants")

    /*
    @GetMapping("/variants/{variantId}") // Kiểu ID của Variant là Long
    public ResponseEntity<VariantResponse> getVariantById(@PathVariable Long variantId) { ... }

    @PutMapping("/variants/{variantId}") // Kiểu ID của Variant là Long
    public ResponseEntity<VariantResponse> updateVariant(@PathVariable Long variantId, @RequestBody VariantRequest request) { ... }

    @DeleteMapping("/variants/{variantId}") // Kiểu ID của Variant là Long
    public ResponseEntity<Void> deleteVariant(@PathVariable Long variantId) { ... }
    */
}