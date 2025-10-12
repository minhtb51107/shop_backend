package com.example.demo.product.service;

import com.example.demo.product.dto.request.ProductImageRequest;
import com.example.demo.product.dto.response.ProductImageResponse;

import java.util.List;
import java.util.Optional;

public interface ProductImageService {
    // Lấy tất cả ảnh cho MỘT sản phẩm cụ thể
    List<ProductImageResponse> getImagesForProduct(Integer productId);

    Optional<ProductImageResponse> getImageById(Integer imageId);

    // Tạo một ảnh cho MỘT sản phẩm cụ thể
    ProductImageResponse createImageForProduct(Integer productId, ProductImageRequest request);

    Optional<ProductImageResponse> updateImage(Integer imageId, ProductImageRequest request);

    boolean deleteImage(Integer imageId);
}