package com.example.demo.user.repository;

import com.example.demo.user.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ProductCategory, Integer> {
}
