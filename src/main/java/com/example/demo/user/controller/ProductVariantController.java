package com.example.demo.user.controller;

import com.example.demo.user.entity.ProductVariant;
import com.example.demo.user.repository.VariantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variants")
public class ProductVariantController {

    private final VariantRepository variantRepository;

    public ProductVariantController(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    @GetMapping
    public List<ProductVariant> getAllVariants() {
        return variantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getVariantById(@PathVariable Long id) {
        return variantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductVariant createVariant(@RequestBody ProductVariant variant) {
        return variantRepository.save(variant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVariant> updateVariant(@PathVariable Long id, @RequestBody ProductVariant variantDetails) {
        return variantRepository.findById(id)
                .map(variant -> {
                    variant.setSku(variantDetails.getSku());
                    variant.setColor(variantDetails.getColor());
                    variant.setPrice(variantDetails.getPrice());
                    return ResponseEntity.ok(variantRepository.save(variant));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVariant(@PathVariable Long id) {
        return variantRepository.findById(id)
                .map(variant -> {
                    variantRepository.delete(variant);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
