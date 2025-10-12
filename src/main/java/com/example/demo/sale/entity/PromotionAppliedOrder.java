package com.example.demo.sale.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "promotion_applied_orders")
public class PromotionAppliedOrder {
    @EmbeddedId
    private PromotionAppliedOrderId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("campaignId")
    @JoinColumn(name = "campaign_id")
    private PromotionCampaign promotionCampaign;

    @Column(name = "discount_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal discountAmount;
}