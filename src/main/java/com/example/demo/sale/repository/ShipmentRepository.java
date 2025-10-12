package com.example.demo.sale.repository;

import com.example.demo.sale.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}