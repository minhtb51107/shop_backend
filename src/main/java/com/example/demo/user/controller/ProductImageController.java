package com.example.demo.user.controller;

import com.example.demo.user.entity.ProductImage;
import com.example.demo.user.repository.ProductImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ProductImageController {

    private final ProductImageRepository imageRepository;

    public ProductImageController(ProductImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping
    public List<ProductImage> getAllImages() {
        return imageRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImage> getImageById(@PathVariable Integer id) {
        return imageRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductImage createImage(@RequestBody ProductImage image) {
        return imageRepository.save(image);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImage> updateImage(@PathVariable Integer id, @RequestBody ProductImage imageDetails) {
        return imageRepository.findById(id)
                .map(img -> {
                    img.setImageUrl(imageDetails.getImageUrl());
                    img.setAltText(imageDetails.getAltText());
                    img.setIsMain(imageDetails.getIsMain());
                    img.setDisplayOrder(imageDetails.getDisplayOrder());
                    img.setMetadata(imageDetails.getMetadata());
                    img.setProduct(imageDetails.getProduct());
                    return ResponseEntity.ok(imageRepository.save(img));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteImage(@PathVariable Integer id) {
        return imageRepository.findById(id)
                .map(img -> {
                    imageRepository.delete(img);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
