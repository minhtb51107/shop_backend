package com.example.demo.supplychain.repository;

import com.example.demo.supplychain.entity.PurchaseOrder;
import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    // Tìm tất cả đơn hàng có phân trang
    Page<PurchaseOrder> findAll(Pageable pageable);

    // Tìm các đơn hàng theo trạng thái và có phân trang
    Page<PurchaseOrder> findByStatus(PurchaseOrderStatus status, Pageable pageable);
}