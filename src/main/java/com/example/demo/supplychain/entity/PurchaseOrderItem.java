//package com.example.demo.supplychain.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.math.BigDecimal;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "supplychain_purchase_order_items") // Thêm prefix để tránh conflict
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//public class PurchaseOrderItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "purchase_order_id", nullable = false)
//    @JsonIgnoreProperties({"items"}) // Tránh circular reference
//    private PurchaseOrder purchaseOrder;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "variant_id", nullable = false)
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    private ProductVariant variant; // Tham chiếu đến Entity ProductVariant
//
//    @Column(nullable = false)
//    private Integer quantity;
//
//    @Column(name = "unit_price", nullable = false, precision = 18, scale = 2)
//    private BigDecimal unitPrice;
//}