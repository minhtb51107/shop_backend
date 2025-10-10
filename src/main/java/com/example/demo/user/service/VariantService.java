package com.example.demo.user.service;

import com.example.demo.user.entity.ProductVariant;
import java.util.List;
import java.util.Optional;

public interface VariantService {
    List<ProductVariant> findAll();
    Optional<ProductVariant> findById(Long id);
    ProductVariant save(ProductVariant variant);
    void deleteById(Long id);
}
