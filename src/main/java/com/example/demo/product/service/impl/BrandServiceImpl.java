package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.BrandRequest;
import com.example.demo.product.dto.response.BrandResponse;
import com.example.demo.product.entity.Brand;
import com.example.demo.product.mapper.BrandMapper;
import com.example.demo.product.repository.BrandRepository;
import com.example.demo.product.service.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BrandResponse> getBrandById(Integer id) {
        return brandRepository.findById(id)
                .map(brandMapper::toResponse);
    }

    @Override
    @Transactional
    public BrandResponse createBrand(BrandRequest request) {
        Brand brand = brandMapper.toEntity(request);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toResponse(savedBrand);
    }

    @Override
    @Transactional
    public Optional<BrandResponse> updateBrand(Integer id, BrandRequest request) {
        return brandRepository.findById(id)
                .map(existingBrand -> {
                    brandMapper.updateEntity(existingBrand, request);
                    Brand updatedBrand = brandRepository.save(existingBrand);
                    return brandMapper.toResponse(updatedBrand);
                });
    }

    @Override
    @Transactional
    public boolean deleteBrand(Integer id) {
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
