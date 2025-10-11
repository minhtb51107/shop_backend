package com.example.demo.product.repository;

import com.example.demo.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<ProductVariant, Long> {
}