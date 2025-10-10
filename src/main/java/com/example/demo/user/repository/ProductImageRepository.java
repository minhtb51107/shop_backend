package com.example.demo.user.repository;

import com.example.demo.user.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductIdOrderByIsMainDesc(Integer productId);
}
