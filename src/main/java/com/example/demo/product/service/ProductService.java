package com.example.demo.product.service;

import com.example.demo.product.dto.request.ProductRequest;
import com.example.demo.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.Pageable; // Import Pageable

import java.util.List;
import java.util.Optional;

public interface ProductService {
    // --- SỬA PHƯƠNG THỨC NÀY ---
    Page<ProductResponse> getAllProducts(Pageable pageable, String search, Integer categoryId, Integer brandId);

    Optional<ProductResponse> getProductById(Integer id);
    ProductResponse createProduct(ProductRequest request);
    Optional<ProductResponse> updateProduct(Integer id, ProductRequest request);
    boolean deleteProduct(Integer id);
    List<ProductResponse> getRelatedProducts(Integer productId, int limit);
}