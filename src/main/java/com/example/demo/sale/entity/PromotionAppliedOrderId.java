package com.example.demo.sale.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class PromotionAppliedOrderId implements Serializable {
    private Long orderId;
    private Integer campaignId;
}