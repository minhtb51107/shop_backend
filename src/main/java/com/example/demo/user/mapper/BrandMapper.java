package com.example.demo.user.mapper;

import com.example.demo.user.dto.request.BrandRequest;
import com.example.demo.user.dto.response.BrandResponse;
import com.example.demo.user.entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public Brand toEntity(BrandRequest request) {
        if (request == null) return null;
        Brand brand = new Brand();
        brand.setId(request.getId());
        brand.setName(request.getName());
        return brand;
    }

    public BrandResponse toResponse(Brand entity) {
        if (entity == null) return null;
        BrandResponse response = new BrandResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        return response;
    }
}
