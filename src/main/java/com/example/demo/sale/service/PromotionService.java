package com.example.demo.sale.service;

import com.example.demo.sale.dto.request.CreatePromotionRequest;
import com.example.demo.sale.dto.response.PromotionResponse;
import com.example.demo.sale.entity.PromotionCampaign;

import java.util.List;

public interface PromotionService {
    PromotionResponse createPromotion(CreatePromotionRequest request);
    PromotionResponse getPromotionById(Integer id);
    List<PromotionResponse> getAllPromotions();
    void updatePromotionStatus(Integer id, boolean isActive);
}