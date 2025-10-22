package com.example.demo.product.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List; // <-- GIỮ LẠI IMPORT NÀY
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

    // ----- BỎ CÁC TRƯỜNG ĐẠI DIỆN NÀY -----
    // private BigDecimal price; // Bỏ đi, sẽ lấy từ list variants
    // private String imageUrl; // Bỏ đi, sẽ lấy từ list images

    // ----- THÊM DANH SÁCH ĐẦY ĐỦ -----
    // Giả sử bạn đã có VariantResponse và ProductImageResponse DTOs
    private List<VariantResponse> variants;      // <--- THÊM DÒNG NÀY
    private List<ProductImageResponse> images;   // <--- THÊM DÒNG NÀY
    // ----- KẾT THÚC THÊM DANH SÁCH -----

    private Map<String, String> specs;
}