package com.example.demo.product.mapper;

import com.example.demo.product.dto.request.BrandRequest;
import com.example.demo.product.dto.response.BrandResponse;
import com.example.demo.product.entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    /**
     * Chuyển đổi từ BrandRequest DTO sang Brand Entity để tạo mới.
     * @param request DTO chứa thông tin từ client.
     * @return một đối tượng Brand mới.
     */
    public Brand toEntity(BrandRequest request) {
        if (request == null) {
            return null;
        }
        Brand brand = new Brand();
        brand.setName(request.getName());
        return brand;
    }

    /**
     * Chuyển đổi từ Brand Entity sang BrandResponse DTO để trả về cho client.
     * @param entity Đối tượng Brand từ database.
     * @return một đối tượng DTO chứa thông tin an toàn để hiển thị.
     */
    public BrandResponse toResponse(Brand entity) {
        if (entity == null) {
            return null;
        }
        BrandResponse response = new BrandResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        return response;
    }

    /**
     * Cập nhật thông tin từ BrandRequest DTO vào một Brand Entity đã tồn tại.
     * @param brand Entity đã có từ database.
     * @param request DTO chứa thông tin cần cập nhật.
     */
    public void updateEntity(Brand brand, BrandRequest request) {
        if (request == null || brand == null) {
            return;
        }
        brand.setName(request.getName());
    }
}
