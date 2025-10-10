package com.example.demo.sale.service.impl;

import com.example.demo.sale.dto.request.CreatePromotionRequest;
import com.example.demo.sale.dto.response.PromotionResponse;
import com.example.demo.sale.entity.PromotionCampaign;
import com.example.demo.sale.mapper.PromotionMapper;
import com.example.demo.sale.repository.PromotionCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionCampaignRepository promotionCampaignRepository;
    private final PromotionMapper promotionMapper;

    @Override
    @Transactional
    public PromotionResponse createPromotion(CreatePromotionRequest request) {
        PromotionCampaign campaign = promotionMapper.toEntity(request);
        PromotionCampaign savedCampaign = promotionCampaignRepository.save(campaign);
        return promotionMapper.toDto(savedCampaign);
    }

    @Override
    public PromotionResponse getPromotionById(Integer id) {
        PromotionCampaign campaign = promotionCampaignRepository.findById(id).orElseThrow();
        return promotionMapper.toDto(campaign);
    }

    @Override
    public List<PromotionResponse> getAllPromotions() {
        return promotionCampaignRepository.findAll().stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updatePromotionStatus(Integer id, boolean isActive) {
        PromotionCampaign campaign = promotionCampaignRepository.findById(id).orElseThrow();
        campaign.setIsActive(isActive);
        promotionCampaignRepository.save(campaign);
    }
}