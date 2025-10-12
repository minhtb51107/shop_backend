package com.example.demo.sale.repository;

import com.example.demo.sale.entity.PromotionAppliedOrder;
import com.example.demo.sale.entity.PromotionAppliedOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionAppliedOrderRepository extends JpaRepository<PromotionAppliedOrder, PromotionAppliedOrderId> {
}