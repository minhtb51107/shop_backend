package com.example.demo.sale.repository;

import com.example.demo.sale.entity.ReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnItemRepository extends JpaRepository<ReturnItem, Long> {
}