package com.example.demo.user.mapper;

import com.example.demo.user.dto.request.VariantRequest;
import com.example.demo.user.dto.response.VariantResponse;
import com.example.demo.user.entity.ProductVariant;

public class VariantMapper {

    public static ProductVariant toEntity(VariantRequest req) {
        ProductVariant variant = new ProductVariant();
        variant.setSku(req.getSku());
        variant.setPrice(req.getPrice());
        variant.setColor(req.getColor());
        return variant;
    }

    public static VariantResponse toResponse(ProductVariant entity) {
        VariantResponse res = new VariantResponse();
        res.setId(entity.getId());
        res.setSku(entity.getSku());
        res.setPrice(entity.getPrice());
        res.setColor(entity.getColor());
        if (entity.getProduct() != null) {
            res.setProductName(entity.getProduct().getName());
        }
        return res;
    }
}
