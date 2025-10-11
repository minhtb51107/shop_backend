package com.example.demo.product_inventory.service;

import com.example.demo.product_inventory.dto.request.ProductCategoryRequest;
import com.example.demo.product_inventory.dto.response.ProductCategoryResponse;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {
    List<ProductCategoryResponse> getAllCategories();
    Optional<ProductCategoryResponse> getCategoryById(Integer id);
    ProductCategoryResponse createCategory(ProductCategoryRequest request);
    Optional<ProductCategoryResponse> updateCategory(Integer id, ProductCategoryRequest request);
    boolean deleteCategory(Integer id);
}