// File: repository/GoodsReceiptRepository.java
package com.example.demo.product_inventory.repository;

import com.example.demo.product_inventory.entity.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long> {
}