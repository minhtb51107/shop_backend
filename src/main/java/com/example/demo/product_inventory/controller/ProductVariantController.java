package com.example.demo.product_inventory.controller;

import com.example.demo.product_inventory.dto.request.VariantRequest;
import com.example.demo.product_inventory.dto.response.VariantResponse;
import com.example.demo.product_inventory.service.VariantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Base path chung
public class ProductVariantController {

    private final VariantService variantService;

    public ProductVariantController(VariantService variantService) {
        this.variantService = variantService;
    }

    /**
     * Lấy tất cả các biến thể của một sản phẩm cụ thể.
     */
    @GetMapping("/products/{productId}/variants")
    public ResponseEntity<List<VariantResponse>> getVariantsForProduct(@PathVariable Integer productId) {
        List<VariantResponse> variants = variantService.getVariantsForProduct(productId);
        return ResponseEntity.ok(variants);
    }

    /**
     * Tạo một biến thể mới cho một sản phẩm cụ thể.
     */
    @PostMapping("/products/{productId}/variants")
    public ResponseEntity<VariantResponse> createVariantForProduct(
            @PathVariable Integer productId,
            @RequestBody VariantRequest request) {
        VariantResponse newVariant = variantService.createVariantForProduct(productId, request);
        return new ResponseEntity<>(newVariant, HttpStatus.CREATED);
    }

    /**
     * Lấy một biến thể cụ thể bằng ID của nó.
     */
    @GetMapping("/variants/{variantId}")
    public ResponseEntity<VariantResponse> getVariantById(@PathVariable Long variantId) {
        return variantService.getVariantById(variantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cập nhật thông tin một biến thể cụ thể.
     */
    @PutMapping("/variants/{variantId}")
    public ResponseEntity<VariantResponse> updateVariant(
            @PathVariable Long variantId,
            @RequestBody VariantRequest request) {
        return variantService.updateVariant(variantId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Xóa một biến thể cụ thể.
     */
    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<Void> deleteVariant(@PathVariable Long variantId) {
        if (variantService.deleteVariant(variantId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}