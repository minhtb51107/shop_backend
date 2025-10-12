package com.example.demo.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageRequest {
    private String imageUrl;
    private String altText;
    private Boolean isMain;
    private Integer displayOrder;
    private String metadata;
    // productId sẽ được lấy từ URL, không cần truyền trong body
}