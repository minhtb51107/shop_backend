package com.example.demo.product_inventory.mapper;

import com.example.demo.product_inventory.dto.request.ProductRequest;
import com.example.demo.product_inventory.dto.response.BrandResponse;
import com.example.demo.product_inventory.dto.response.ProductCategoryResponse;
import com.example.demo.product_inventory.dto.response.ProductResponse;
import com.example.demo.product_inventory.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        if (request == null) return null;
        Product product = new Product();
        product.setName(request.getName());
        product.setSkuPrefix(request.getSkuPrefix());
        product.setDescription(request.getDescription());
        product.setIsActive(request.getIsActive());
        return product;
    }

    public void updateEntityFromRequest(ProductRequest request, Product product) {
        if (request == null || product == null) return;
        product.setName(request.getName());
        product.setSkuPrefix(request.getSkuPrefix());
        product.setDescription(request.getDescription());
        product.setIsActive(request.getIsActive());
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) return null;

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSkuPrefix(product.getSkuPrefix());
        response.setDescription(product.getDescription());
        response.setIsActive(product.getIsActive());

        if (product.getBrand() != null) {
            BrandResponse brandResponse = new BrandResponse();
            brandResponse.setId(product.getBrand().getId());
            brandResponse.setName(product.getBrand().getName());
            response.setBrand(brandResponse);
        }

        if (product.getCategory() != null) {
            ProductCategoryResponse categoryResponse = new ProductCategoryResponse();
            categoryResponse.setId(product.getCategory().getId());
            categoryResponse.setName(product.getCategory().getName());
            response.setCategory(categoryResponse);
        }

        return response;
    }
}