package com.example.demo.user.service;

import com.example.demo.user.entity.ProductCategory;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<ProductCategory> findAll();
    Optional<ProductCategory> findById(Integer id);
    ProductCategory save(ProductCategory category);
    void deleteById(Integer id);
}
