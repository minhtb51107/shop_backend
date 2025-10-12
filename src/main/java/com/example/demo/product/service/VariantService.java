package com.example.demo.product.service;

import com.example.demo.product.dto.request.VariantRequest;
import com.example.demo.product.dto.response.VariantResponse;

import java.util.List;
import java.util.Optional;

public interface VariantService {
    List<VariantResponse> getVariantsForProduct(Integer productId);

    Optional<VariantResponse> getVariantById(Long variantId);

    VariantResponse createVariantForProduct(Integer productId, VariantRequest request);

    Optional<VariantResponse> updateVariant(Long variantId, VariantRequest request);

    boolean deleteVariant(Long variantId);
}