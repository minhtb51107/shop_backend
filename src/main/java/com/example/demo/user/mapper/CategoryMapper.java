package com.example.demo.user.mapper;

import com.example.demo.user.dto.request.CategoryRequest;
import com.example.demo.user.dto.response.CategoryResponse;
import com.example.demo.user.entity.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public ProductCategory toEntity(CategoryRequest request) {
        if (request == null) return null;
        ProductCategory category = new ProductCategory();
        category.setId(request.getId());
        category.setName(request.getName());
        return category;
    }

    public CategoryResponse toResponse(ProductCategory entity) {
        if (entity == null) return null;
        CategoryResponse response = new CategoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        return response;
    }
}
