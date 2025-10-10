package com.example.demo.user.service;

import com.example.demo.user.entity.ProductImage;

import java.util.List;
import java.util.Optional;

public interface ProductImageService {
    List<ProductImage> findAll();

    Optional<ProductImage> findById(Integer id);

    ProductImage save(ProductImage image);

    void deleteById(Integer id);
}
