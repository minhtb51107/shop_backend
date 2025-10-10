package com.example.demo.user.service.impl;

import com.example.demo.user.entity.Brand;
import com.example.demo.user.repository.BrandRepository;
import com.example.demo.user.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(Integer id) {
        return brandRepository.findById(id);
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public void deleteById(Integer id) {
        brandRepository.deleteById(id);
    }
}
