package com.example.demo.sale.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
class PromotionAppliedOrderId implements Serializable {
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "campaign_id")
    private Integer campaignId;
}

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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("campaignId")
    @JoinColumn(name = "campaign_id")
    private PromotionCampaign campaign;

    @Column(name = "discount_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal discountAmount;
}