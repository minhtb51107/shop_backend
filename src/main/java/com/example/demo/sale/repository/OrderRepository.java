package com.example.demo.sale.repository;

import com.example.demo.sale.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}