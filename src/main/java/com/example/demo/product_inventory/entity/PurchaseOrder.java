package com.example.demo.product_inventory.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Data
public class PurchaseOrder {

    public enum POStatus {
        DRAFT,      // Nháp
        PENDING,    // Đang chờ xử lý
        ORDERED,    // Đã đặt hàng
        PARTIAL,    // Đã nhận một phần
        COMPLETED,  // Hoàn thành
        CANCELLED   // Đã hủy
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    // SỬA LẠI TÊN THUỘC TÍNH VÀ ÁNH XẠ CỘT
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    // SỬA LẠI TÊN THUỘC TÍNH VÀ ÁNH XẠ CỘT
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private POStatus status;

    // SỬA LẠI TÊN THUỘC TÍNH VÀ ÁNH XẠ CỘT
    @Column(name = "created_by_employee_id")
    private Integer createdByEmployeeId;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (orderDate == null) {
            orderDate = LocalDate.now();
        }
        if (status == null) {
            status = POStatus.DRAFT;
        }
    }
}