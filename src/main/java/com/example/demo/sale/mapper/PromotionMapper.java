package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreatePromotionRequest;
import com.example.demo.sale.dto.response.PromotionResponse;
import com.example.demo.sale.entity.PromotionCampaign;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PromotionMapper {
    public PromotionCampaign toEntity(CreatePromotionRequest request) {
        if (request == null) {
            return null;
        }
        PromotionCampaign campaign = new PromotionCampaign();
        campaign.setName(request.getName());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());
        campaign.setDiscountType(request.getDiscountType());
        campaign.setDiscountValue(request.getDiscountValue());
        return campaign;
    }

    public PromotionResponse toDto(PromotionCampaign campaign) {
        if (campaign == null) {
            return null;
        }
        PromotionResponse dto = new PromotionResponse();
        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setDiscountType(campaign.getDiscountType());
        dto.setDiscountValue(campaign.getDiscountValue());
        dto.setIsActive(campaign.getIsActive());
        return dto;
    }

    public List<PromotionResponse> toDtoList(List<PromotionCampaign> campaigns) {
        return campaigns.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}