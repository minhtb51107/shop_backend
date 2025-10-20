package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.VariantRequest;
import com.example.demo.product.dto.response.VariantResponse;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.ProductVariant;
import com.example.demo.product.mapper.VariantMapper;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.repository.VariantRepository;
import com.example.demo.product.service.VariantService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections; // <<< THÊM IMPORT NÀY

@Service
@RequiredArgsConstructor
@Slf4j
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final VariantMapper variantMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VariantResponse> getVariantsForProduct(Integer productId) { // <<< Đã đúng là Integer
        log.debug("Service: Fetching variants for product ID: {}", productId);
        if (!productRepository.existsById(productId)) { // <<< Gọi với Integer
            log.warn("Service: Product not found with id: {}", productId);
            // throw new EntityNotFoundException("Product not found with id: " + productId);
             return Collections.emptyList(); // Trả về rỗng thay vì ném lỗi
        }
        // Giả sử repository có findByProductId(Integer productId) hoặc findByProduct_Id(Integer productId)
        List<ProductVariant> variants = variantRepository.findByProductId(productId); // <<< Gọi với Integer
        log.debug("Service: Found {} variants for product ID {}", variants.size(), productId);
        return variants.stream()
                .map(variantMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VariantResponse> getVariantById(Long variantId) {
        return variantRepository.findById(variantId).map(variantMapper::toResponse);
    }

    @Override
    @Transactional
    public VariantResponse createVariantForProduct(Integer productId, VariantRequest request) { // <<< Đã đúng là Integer
        log.debug("Service: Creating variant for product ID: {}", productId);
        Product product = productRepository.findById(productId) // <<< Gọi với Integer
                .orElseThrow(() -> {
                     log.error("Service: Cannot create variant, product not found with id: {}", productId);
                     return new EntityNotFoundException("Cannot create variant for non-existent product with id: " + productId);
                 });
        // ... (phần còn lại giữ nguyên)
         ProductVariant newVariant = new ProductVariant();
         variantMapper.updateEntityFromRequest(request, newVariant);
         newVariant.setProduct(product);
         ProductVariant savedVariant = variantRepository.save(newVariant);
         log.info("Service: Created variant with ID {} for product ID {}", savedVariant.getId(), productId);
         return variantMapper.toResponse(savedVariant);
    }

    @Override
    @Transactional
    public Optional<VariantResponse> updateVariant(Long variantId, VariantRequest request) {
        return variantRepository.findById(variantId)
                .map(existingVariant -> {
                    variantMapper.updateEntityFromRequest(request, existingVariant);
                    ProductVariant updatedVariant = variantRepository.save(existingVariant);
                    return variantMapper.toResponse(updatedVariant);
                });
    }

    @Override
    @Transactional
    public boolean deleteVariant(Long variantId) {
        if (variantRepository.existsById(variantId)) {
            variantRepository.deleteById(variantId);
            return true;
        }
        return false;
    }
}