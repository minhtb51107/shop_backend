package com.example.demo.product.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal; // <-- THÊM IMPORT
import java.util.List; // <-- THÊM IMPORT (nếu dùng images)
import java.util.Map;

@Getter
@Setter
public class ProductResponse {
    private Integer id;
    private String name;
    private String skuPrefix;
    private String description;
    private Boolean isActive;

    private BrandResponse brand;
    private ProductCategoryResponse category;

    // --- THÊM CÁC TRƯỜNG MỚI ---
    private BigDecimal price;    // Giá đại diện (ví dụ: từ variant đầu tiên)
    private String imageUrl;   // URL ảnh đại diện (ví dụ: ảnh isMain=true hoặc ảnh đầu tiên)
    // --- KẾT THÚC THÊM ---

    // (Tùy chọn) Giữ lại danh sách đầy đủ nếu cần
    // private List<VariantResponse> variants;
    // private List<ProductImageResponse> images;

    private Map<String, String> specs;
}