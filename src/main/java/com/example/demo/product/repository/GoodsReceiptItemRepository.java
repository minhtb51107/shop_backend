// File: repository/GoodsReceiptItemRepository.java
package com.example.demo.product.repository;

import com.example.demo.product.entity.GoodsReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsReceiptItemRepository extends JpaRepository<GoodsReceiptItem, Long> {
}