package com.example.demo.sale.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PromotionAppliedOrderId implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "campaign_id")
    private Integer campaignId;

    public PromotionAppliedOrderId() {}

    public PromotionAppliedOrderId(Long orderId, Integer campaignId) {
        this.orderId = orderId;
        this.campaignId = campaignId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromotionAppliedOrderId that)) return false;
        return Objects.equals(orderId, that.orderId) &&
               Objects.equals(campaignId, that.campaignId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, campaignId);
    }
}
