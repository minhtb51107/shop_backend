package com.example.demo.product.controller;

import com.example.demo.product.dto.request.ProductCategoryRequest;
import com.example.demo.product.dto.response.ProductCategoryResponse;
import com.example.demo.product.service.ProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryResponse> getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductCategoryResponse> createCategory(@RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse newCategory = categoryService.createCategory(request);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryResponse> updateCategory(@PathVariable Integer id, @RequestBody ProductCategoryRequest request) {
        return categoryService.updateCategory(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        boolean isDeleted = categoryService.deleteCategory(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}