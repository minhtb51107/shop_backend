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
//@Table(name = "supplychain_goods_receipt_items") // Thêm prefix để tránh conflict
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//public class GoodsReceiptItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "goods_receipt_id", nullable = false)
//    private GoodsReceipt goodsReceipt;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "po_item_id")
//    private PurchaseOrderItem purchaseOrderItem;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "variant_id", nullable = false)
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    private ProductVariant variant; // Tham chiếu đến Entity ProductVariant
//
//    @Column(name = "quantity_received", nullable = false)
//    private Integer quantityReceived;
//
//    @Column(name = "unit_cost", precision = 18, scale = 2)
//    private BigDecimal unitCost;
//}