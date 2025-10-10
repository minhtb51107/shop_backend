package com.example.demo.user.service.impl;

import com.example.demo.user.entity.ProductVariant;
import com.example.demo.user.repository.VariantRepository;
import com.example.demo.user.service.VariantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;

    public VariantServiceImpl(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    @Override
    public List<ProductVariant> findAll() {
        return variantRepository.findAll();
    }

    @Override
    public Optional<ProductVariant> findById(Long id) {
        return variantRepository.findById(id);
    }

    @Override
    public ProductVariant save(ProductVariant variant) {
        return variantRepository.save(variant);
    }

    @Override
    public void deleteById(Long id) {
        variantRepository.deleteById(id);
    }
}
