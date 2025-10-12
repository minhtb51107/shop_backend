package com.example.demo.product.repository;

import com.example.demo.product.entity.ProductSpecsLaptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSpecsLaptopRepository extends JpaRepository<ProductSpecsLaptop, Integer> {
}