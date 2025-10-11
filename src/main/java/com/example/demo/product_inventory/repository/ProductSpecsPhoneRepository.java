package com.example.demo.product_inventory.repository;

import com.example.demo.product_inventory.entity.ProductSpecsPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSpecsPhoneRepository extends JpaRepository<ProductSpecsPhone, Integer> {
}