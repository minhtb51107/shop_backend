// src/main/java/com/example/demo/supplychain/entity/PurchaseOrder.java
package com.example.demo.supplychain.entity;

import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import com.example.demo.user.entity.Employee;
import com.example.demo.product.entity.PurchaseOrderItem; // THÊM IMPORT NÀY
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "purchase_orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Supplier supplier;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private PurchaseOrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee createdBy;

    // *** BỎ COMMENT VÀ CẬP NHẬT MAPPING ***
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"purchaseOrder"}) // Tránh circular reference
    private List<PurchaseOrderItem> items;
}