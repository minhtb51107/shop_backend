package com.example.demo.user.repository;

import com.example.demo.user.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<ProductVariant, Long> {
}
