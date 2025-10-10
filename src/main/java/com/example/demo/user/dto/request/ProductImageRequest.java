package com.example.demo.user.dto.request;

import lombok.Data;

@Data
public class ProductImageRequest {
    private Integer productId;
    private String imageUrl;
    private String altText;
    private Boolean isMain;
    private Integer displayOrder;
    private String metadata;
}
