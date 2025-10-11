package com.example.demo.product_inventory.service.impl;

import com.example.demo.product_inventory.dto.request.ProductImageRequest;
import com.example.demo.product_inventory.dto.response.ProductImageResponse;
import com.example.demo.product_inventory.entity.Product;
import com.example.demo.product_inventory.entity.ProductImage;
import com.example.demo.product_inventory.mapper.ProductImageMapper;
import com.example.demo.product_inventory.repository.ProductImageRepository;
import com.example.demo.product_inventory.repository.ProductRepository;
import com.example.demo.product_inventory.service.ProductImageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper imageMapper;

    public ProductImageServiceImpl(ProductImageRepository imageRepository,
                                   ProductRepository productRepository,
                                   ProductImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.imageMapper = imageMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getImagesForProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException("Product not found with id: " + productId);
        }
        return imageRepository.findByProductIdOrderByIsMainDesc(productId)
                .stream()
                .map(imageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductImageResponse> getImageById(Integer imageId) {
        return imageRepository.findById(imageId).map(imageMapper::toResponse);
    }

    @Override
    @Transactional
    public ProductImageResponse createImageForProduct(Integer productId, ProductImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot create image for non-existent product with id: " + productId));

        ProductImage newImage = new ProductImage();
        imageMapper.updateEntityFromRequest(request, newImage);
        newImage.setProduct(product);

        ProductImage savedImage = imageRepository.save(newImage);
        return imageMapper.toResponse(savedImage);
    }

    @Override
    @Transactional
    public Optional<ProductImageResponse> updateImage(Integer imageId, ProductImageRequest request) {
        return imageRepository.findById(imageId)
                .map(existingImage -> {
                    imageMapper.updateEntityFromRequest(request, existingImage);
                    ProductImage updatedImage = imageRepository.save(existingImage);
                    return imageMapper.toResponse(updatedImage);
                });
    }

    @Override
    @Transactional
    public boolean deleteImage(Integer imageId) {
        if (imageRepository.existsById(imageId)) {
            imageRepository.deleteById(imageId);
            return true;
        }
        return false;
    }
}