package com.example.demo.user.mapper;

import com.example.demo.user.dto.request.ProductImageRequest;
import com.example.demo.user.dto.response.ProductImageResponse;
import com.example.demo.user.entity.ProductImage;

public class ProductImageMapper {

    public static ProductImage toEntity(ProductImageRequest req) {
        ProductImage img = new ProductImage();
        img.setImageUrl(req.getImageUrl());
        img.setAltText(req.getAltText());
        img.setIsMain(req.getIsMain());
        img.setDisplayOrder(req.getDisplayOrder());
        img.setMetadata(req.getMetadata());
        return img;
    }

    public static ProductImageResponse toResponse(ProductImage entity) {
        ProductImageResponse res = new ProductImageResponse();
        res.setId(entity.getId());
        res.setImageUrl(entity.getImageUrl());
        res.setAltText(entity.getAltText());
        res.setIsMain(entity.getIsMain());
        res.setDisplayOrder(entity.getDisplayOrder());
        res.setMetadata(entity.getMetadata());
        res.setCreatedAt(entity.getCreatedAt());
        if (entity.getProduct() != null) {
            res.setProductName(entity.getProduct().getName());
        }
        return res;
    }
}
