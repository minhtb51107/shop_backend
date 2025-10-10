package com.example.demo.user.service.impl;

import com.example.demo.user.entity.ProductImage;
import com.example.demo.user.repository.ProductImageRepository;
import com.example.demo.user.service.ProductImageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository imageRepository;

    public ProductImageServiceImpl(ProductImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<ProductImage> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public Optional<ProductImage> findById(Integer id) {
        return imageRepository.findById(id);
    }

    @Override
    public ProductImage save(ProductImage image) {
        return imageRepository.save(image);
    }

    @Override
    public void deleteById(Integer id) {
        imageRepository.deleteById(id);
    }
}
