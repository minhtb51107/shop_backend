//package com.example.demo.supplychain.controller;
//
//import com.example.demo.supplychain.entity.ProductVariant;
//import com.example.demo.supplychain.service.ProductVariantService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/v1/product-variants")
//@RequiredArgsConstructor
//public class ProductVariantController {
//
//    private final ProductVariantService productVariantService;
//
//    @GetMapping
//    public ResponseEntity<List<ProductVariant>> getAllProductVariants() {
//        List<ProductVariant> variants = productVariantService.getAllProductVariants();
//        return ResponseEntity.ok(variants);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductVariant> getProductVariantById(@PathVariable Integer id) {
//        Optional<ProductVariant> variant = productVariantService.getProductVariantById(id);
//        return variant.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/sku/{sku}")
//    public ResponseEntity<ProductVariant> getProductVariantBySku(@PathVariable String sku) {
//        Optional<ProductVariant> variant = productVariantService.getProductVariantBySku(sku);
//        return variant.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public ResponseEntity<ProductVariant> createProductVariant(@RequestBody ProductVariant productVariant) {
//        ProductVariant savedVariant = productVariantService.createProductVariant(productVariant);
//        return ResponseEntity.ok(savedVariant);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ProductVariant> updateProductVariant(@PathVariable Integer id, @RequestBody ProductVariant variantDetails) {
//        try {
//            ProductVariant updatedVariant = productVariantService.updateProductVariant(id, variantDetails);
//            return ResponseEntity.ok(updatedVariant);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProductVariant(@PathVariable Integer id) {
//        try {
//            productVariantService.deleteProductVariant(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
