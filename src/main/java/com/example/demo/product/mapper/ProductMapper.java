package com.example.demo.product.mapper;

import com.example.demo.product.dto.request.ProductRequest;
import com.example.demo.product.dto.response.BrandResponse;
import com.example.demo.product.dto.response.ProductCategoryResponse;
import com.example.demo.product.dto.response.ProductResponse;
import com.example.demo.product.dto.response.VariantResponse;
import com.example.demo.product.dto.response.ProductImageResponse;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.ProductImage;
import com.example.demo.product.entity.ProductVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final VariantMapper variantMapper;
    private final ProductImageMapper productImageMapper;

    @Autowired
    public ProductMapper(VariantMapper variantMapper, ProductImageMapper productImageMapper) {
        this.variantMapper = variantMapper;
        this.productImageMapper = productImageMapper;
    }

    // Hàm toEntity giữ nguyên
    public Product toEntity(ProductRequest request) {
        if (request == null) return null;
        Product product = new Product();
        product.setName(request.getName());
        product.setSkuPrefix(request.getSkuPrefix());
        product.setDescription(request.getDescription());
        product.setIsActive(request.getIsActive());
        return product;
    }

    // Hàm updateEntityFromRequest giữ nguyên
    public void updateEntityFromRequest(ProductRequest request, Product product) {
        if (request == null || product == null) return;
        product.setName(request.getName());
        product.setSkuPrefix(request.getSkuPrefix());
        product.setDescription(request.getDescription());
        product.setIsActive(request.getIsActive());
    }

    // ----- HÀM toResponse CHÍNH (Giữ nguyên như lần sửa trước) -----
    /**
     * Map từ Product Entity (đã fetch đủ variants, images) sang ProductResponse DTO.
     * Sử dụng VariantMapper và ProductImageMapper để map các danh sách.
     */
    public ProductResponse toResponse(Product product, List<ProductVariant> variants, List<ProductImage> images) {
        if (product == null) return null;

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSkuPrefix(product.getSkuPrefix());
        response.setDescription(product.getDescription());
        response.setIsActive(product.getIsActive());

        // Mapping Brand (giữ nguyên)
        if (product.getBrand() != null) {
            BrandResponse brandResponse = new BrandResponse();
            brandResponse.setId(product.getBrand().getId());
            brandResponse.setName(product.getBrand().getName());
            response.setBrand(brandResponse);
        }
        // Mapping Category (giữ nguyên)
        if (product.getCategory() != null) {
            ProductCategoryResponse categoryResponse = new ProductCategoryResponse();
            categoryResponse.setId(product.getCategory().getId());
            categoryResponse.setName(product.getCategory().getName());
            response.setCategory(categoryResponse);
        }

        // Map List<ProductVariant> sang List<VariantResponse>
        if (variants != null) {
            response.setVariants(variants.stream()
                .filter(Objects::nonNull)
                .map(variantMapper::toResponse)
                .collect(Collectors.toList()));
        } else {
            response.setVariants(Collections.emptyList());
        }

        // Map List<ProductImage> sang List<ProductImageResponse>
        if (images != null) {
            response.setImages(images.stream()
                .filter(Objects::nonNull)
                .map(productImageMapper::toResponse)
                .collect(Collectors.toList()));
        } else {
            response.setImages(Collections.emptyList());
        }

        // Specs sẽ được map trong Service

        return response;
    };

    // ========== XÓA BỎ HOÀN TOÀN HÀM OVERLOAD GÂY LỖI ==========
    // public ProductResponse toResponse(Product product) {
    //     // ... code cũ đã bị xóa ...
    // }
    // ========== KẾT THÚC XÓA BỎ ==========
}