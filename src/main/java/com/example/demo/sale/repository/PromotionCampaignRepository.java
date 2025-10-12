package com.example.demo.sale.repository;

import com.example.demo.sale.entity.PromotionCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionCampaignRepository extends JpaRepository<PromotionCampaign, Integer> {
}