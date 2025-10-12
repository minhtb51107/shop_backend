package com.example.demo.product.controller;

import com.example.demo.product.dto.request.ProductImageRequest;
import com.example.demo.product.dto.response.ProductImageResponse;
import com.example.demo.product.service.ProductImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Base path chung
public class ProductImageController {

    private final ProductImageService imageService;

    public ProductImageController(ProductImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Lấy tất cả ảnh của một sản phẩm cụ thể.
     */
    @GetMapping("/products/{productId}/images")
    public ResponseEntity<List<ProductImageResponse>> getImagesForProduct(@PathVariable Integer productId) {
        List<ProductImageResponse> images = imageService.getImagesForProduct(productId);
        return ResponseEntity.ok(images);
    }

    /**
     * Tạo một ảnh mới cho một sản phẩm cụ thể.
     */
    @PostMapping("/products/{productId}/images")
    public ResponseEntity<ProductImageResponse> createImageForProduct(
            @PathVariable Integer productId,
            @RequestBody ProductImageRequest request) {
        ProductImageResponse newImage = imageService.createImageForProduct(productId, request);
        return new ResponseEntity<>(newImage, HttpStatus.CREATED);
    }

    /**
     * Lấy một ảnh cụ thể bằng ID của ảnh.
     */
    @GetMapping("/images/{imageId}")
    public ResponseEntity<ProductImageResponse> getImageById(@PathVariable Integer imageId) {
        return imageService.getImageById(imageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cập nhật thông tin một ảnh cụ thể.
     */
    @PutMapping("/images/{imageId}")
    public ResponseEntity<ProductImageResponse> updateImage(
            @PathVariable Integer imageId,
            @RequestBody ProductImageRequest request) {
        return imageService.updateImage(imageId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Xóa một ảnh cụ thể.
     */
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer imageId) {
        if (imageService.deleteImage(imageId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}