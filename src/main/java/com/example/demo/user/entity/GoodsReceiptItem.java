package com.example.demo.user.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "goods_receipt_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goods_receipt_id", nullable = false)
    private Long goodsReceiptId;

    @Column(name = "po_item_id")
    private Long purchaseOrderItemId;

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @Column(name = "quantity_received")
    private Integer quantityReceived;

    @Column(name = "unit_cost", precision = 18, scale = 2)
    private BigDecimal unitCost;
}
