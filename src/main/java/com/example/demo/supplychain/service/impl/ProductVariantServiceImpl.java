package com.example.demo.supplychain.service.impl;

import com.example.demo.supplychain.entity.ProductVariant;
import com.example.demo.supplychain.repository.ProductVariantRepository;
import com.example.demo.supplychain.service.ProductVariantService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariant> getAllProductVariants() {
        return productVariantRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariant> getProductVariantById(Integer id) {
        return productVariantRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariant> getProductVariantBySku(String sku) {
        return productVariantRepository.findBySku(sku);
    }

    @Override
    @Transactional
    public ProductVariant createProductVariant(ProductVariant productVariant) {
        return productVariantRepository.save(productVariant);
    }

    @Override
    @Transactional
    public ProductVariant updateProductVariant(Integer id, ProductVariant variantDetails) {
        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with id: " + id));
        
        variant.setSku(variantDetails.getSku());
        variant.setName(variantDetails.getName());
        variant.setDescription(variantDetails.getDescription());
        variant.setUnitPrice(variantDetails.getUnitPrice());
        variant.setCurrentStock(variantDetails.getCurrentStock());
        variant.setMinStockLevel(variantDetails.getMinStockLevel());
        variant.setIsActive(variantDetails.getIsActive());
        
        return productVariantRepository.save(variant);
    }

    @Override
    @Transactional
    public void deleteProductVariant(Integer id) {
        if (!productVariantRepository.existsById(id)) {
            throw new EntityNotFoundException("Product Variant not found with id: " + id);
        }
        productVariantRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return productVariantRepository.existsById(id);
    }
}
