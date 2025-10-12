package com.example.demo.supplychain.repository;

import com.example.demo.supplychain.entity.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Integer> {
}