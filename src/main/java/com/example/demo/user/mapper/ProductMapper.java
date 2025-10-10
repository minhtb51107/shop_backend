package com.example.demo.user.mapper;

import com.example.demo.user.dto.request.ProductRequest;
import com.example.demo.user.dto.response.ProductResponse;
import com.example.demo.user.entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setSkuPrefix(request.getSkuPrefix());
        product.setDescription(request.getDescription());
        product.setIsActive(request.getIsActive());
        product.setIsDeleted(request.getIsDeleted());
        return product;
    }

    public static ProductResponse toResponse(Product entity) {
        ProductResponse res = new ProductResponse();
        res.setId(entity.getId());
        res.setName(entity.getName());
        res.setSkuPrefix(entity.getSkuPrefix());
        res.setDescription(entity.getDescription());
        res.setIsActive(entity.getIsActive());
        res.setIsDeleted(entity.getIsDeleted());
        if (entity.getBrand() != null) {
            res.setBrandName(entity.getBrand().getName());
        }
        if (entity.getCategory() != null) {
            res.setCategoryName(entity.getCategory().getName());
        }
        return res;
    }
}
