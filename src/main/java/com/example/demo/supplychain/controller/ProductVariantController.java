package com.example.demo.supplychain.controller;

import com.example.demo.supplychain.entity.ProductVariant;
import com.example.demo.supplychain.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/product-variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantRepository productVariantRepository;

    @GetMapping
    public ResponseEntity<List<ProductVariant>> getAllProductVariants() {
        List<ProductVariant> variants = productVariantRepository.findAll();
        return ResponseEntity.ok(variants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getProductVariantById(@PathVariable Integer id) {
        Optional<ProductVariant> variant = productVariantRepository.findById(id);
        return variant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductVariant> getProductVariantBySku(@PathVariable String sku) {
        Optional<ProductVariant> variant = productVariantRepository.findBySku(sku);
        return variant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductVariant> createProductVariant(@RequestBody ProductVariant productVariant) {
        ProductVariant savedVariant = productVariantRepository.save(productVariant);
        return ResponseEntity.ok(savedVariant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVariant> updateProductVariant(@PathVariable Integer id, @RequestBody ProductVariant variantDetails) {
        Optional<ProductVariant> optionalVariant = productVariantRepository.findById(id);
        if (optionalVariant.isPresent()) {
            ProductVariant variant = optionalVariant.get();
            variant.setSku(variantDetails.getSku());
            variant.setName(variantDetails.getName());
            variant.setDescription(variantDetails.getDescription());
            variant.setUnitPrice(variantDetails.getUnitPrice());
            variant.setCurrentStock(variantDetails.getCurrentStock());
            variant.setMinStockLevel(variantDetails.getMinStockLevel());
            variant.setIsActive(variantDetails.getIsActive());
            
            ProductVariant updatedVariant = productVariantRepository.save(variant);
            return ResponseEntity.ok(updatedVariant);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductVariant(@PathVariable Integer id) {
        if (productVariantRepository.existsById(id)) {
            productVariantRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
