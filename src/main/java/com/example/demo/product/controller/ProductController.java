package com.example.demo.product.controller;

import com.example.demo.product.dto.request.ProductRequest;
import com.example.demo.product.dto.response.ProductResponse;
import com.example.demo.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.Pageable; // Import Pageable
import org.springframework.data.web.PageableDefault; // Import PageableDefault
import org.springframework.data.domain.Sort; // Import Sort
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping("/{productId}/related")
    public ResponseEntity<List<ProductResponse>> getRelatedProducts(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "4") int limit // Mặc định lấy 4 sản phẩm
    ) {
        List<ProductResponse> relatedProducts = productService.getRelatedProducts(productId, limit);
        return ResponseEntity.ok(relatedProducts);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            // Tham số phân trang và sắp xếp (ví dụ: ?page=0&size=10&sort=name,asc)
            @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            // Tham số lọc
            @RequestParam(name = "search", required = false) String search, // Thêm name = "..."
            @RequestParam(name = "categoryId", required = false) Integer categoryId, // Thêm name = "..."
            @RequestParam(name = "brandId", required = false) Integer brandId // Thêm name = "..."
            // Thêm các @RequestParam khác nếu cần (minPrice, maxPrice...)
    ) {
        Page<ProductResponse> productPage = productService.getAllProducts(pageable, search, categoryId, brandId);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        ProductResponse newProduct = productService.createProduct(request);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id, @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}