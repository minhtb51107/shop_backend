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

@Service
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final VariantMapper variantMapper;

    public VariantServiceImpl(VariantRepository variantRepository,
                                     ProductRepository productRepository,
                                     VariantMapper variantMapper) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
        this.variantMapper = variantMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VariantResponse> getVariantsForProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException("Product not found with id: " + productId);
        }
        // Giả sử VariantRepository có phương thức findByProductId
        // Nếu chưa có, bạn cần thêm vào: List<ProductVariant> findByProductId(Integer productId);
        return variantRepository.findByProductId(productId).stream()
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
    public VariantResponse createVariantForProduct(Integer productId, VariantRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot create variant for non-existent product with id: " + productId));

        ProductVariant newVariant = new ProductVariant();
        variantMapper.updateEntityFromRequest(request, newVariant);
        newVariant.setProduct(product);

        ProductVariant savedVariant = variantRepository.save(newVariant);
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