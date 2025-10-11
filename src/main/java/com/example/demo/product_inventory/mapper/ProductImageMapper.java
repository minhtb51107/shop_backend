package com.example.demo.product_inventory.mapper;

import com.example.demo.product_inventory.dto.request.ProductImageRequest;
import com.example.demo.product_inventory.dto.response.ProductImageResponse;
import com.example.demo.product_inventory.entity.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {

    public ProductImageResponse toResponse(ProductImage entity) {
        if (entity == null) return null;

        ProductImageResponse response = new ProductImageResponse();
        response.setId(entity.getId());
        response.setImageUrl(entity.getImageUrl());
        response.setAltText(entity.getAltText());
        response.setIsMain(entity.getIsMain());
        response.setDisplayOrder(entity.getDisplayOrder());
        response.setMetadata(entity.getMetadata());
        response.setCreatedAt(entity.getCreatedAt());

        if (entity.getProduct() != null) {
            response.setProductId(entity.getProduct().getId());
        }

        return response;
    }

    public void updateEntityFromRequest(ProductImageRequest request, ProductImage entity) {
        if (request == null || entity == null) return;

        entity.setImageUrl(request.getImageUrl());
        entity.setAltText(request.getAltText());
        entity.setIsMain(request.getIsMain());
        entity.setDisplayOrder(request.getDisplayOrder());
        entity.setMetadata(request.getMetadata());
    }
}