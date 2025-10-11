package com.example.demo.product_inventory.mapper;

import com.example.demo.product_inventory.dto.request.VariantRequest;
import com.example.demo.product_inventory.dto.response.VariantResponse;
import com.example.demo.product_inventory.entity.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class VariantMapper {

    public VariantResponse toResponse(ProductVariant entity) {
        if (entity == null) return null;

        VariantResponse response = new VariantResponse();
        response.setId(entity.getId());
        response.setSku(entity.getSku());
        response.setPrice(entity.getPrice());
        response.setColor(entity.getColor());

        if (entity.getProduct() != null) {
            response.setProductId(entity.getProduct().getId());
            response.setProductName(entity.getProduct().getName());
        }

        return response;
    }

    public void updateEntityFromRequest(VariantRequest request, ProductVariant entity) {
        if (request == null || entity == null) return;

        entity.setSku(request.getSku());
        entity.setPrice(request.getPrice());
        entity.setColor(request.getColor());
    }
}