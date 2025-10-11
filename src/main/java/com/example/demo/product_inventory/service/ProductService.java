package com.example.demo.product_inventory.service;

import com.example.demo.product_inventory.dto.request.ProductRequest;
import com.example.demo.product_inventory.dto.response.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    Optional<ProductResponse> getProductById(Integer id);
    ProductResponse createProduct(ProductRequest request);
    Optional<ProductResponse> updateProduct(Integer id, ProductRequest request);
    boolean deleteProduct(Integer id);
}