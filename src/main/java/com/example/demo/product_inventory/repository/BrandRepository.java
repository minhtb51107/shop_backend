package com.example.demo.product_inventory.repository;

import com.example.demo.product_inventory.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
