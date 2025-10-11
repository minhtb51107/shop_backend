package com.example.demo.product_inventory.repository;

import com.example.demo.product_inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}

