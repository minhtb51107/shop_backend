package com.example.demo.product_inventory.mapper;

import com.example.demo.product_inventory.dto.request.ProductCategoryRequest;
import com.example.demo.product_inventory.dto.response.ProductCategoryResponse;
import com.example.demo.product_inventory.entity.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryMapper {

    // Chuyển từ Request DTO sang Entity để lưu vào DB
    public ProductCategory toEntity(ProductCategoryRequest request) {
        if (request == null) {
            return null;
        }
        ProductCategory category = new ProductCategory();
        category.setName(request.getName());
        return category;
    }

    // Cập nhật một Entity đã có từ Request DTO
    public void updateEntityFromRequest(ProductCategoryRequest request, ProductCategory entity) {
        if (request == null || entity == null) {
            return;
        }
        entity.setName(request.getName());
    }

    // Chuyển từ Entity sang Response DTO để trả về cho client
    public ProductCategoryResponse toResponse(ProductCategory entity) {
        if (entity == null) {
            return null;
        }
        ProductCategoryResponse response = new ProductCategoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        return response;
    }
}