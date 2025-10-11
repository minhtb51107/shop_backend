package com.example.demo.product_inventory.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ProductImageResponse {
    private Integer id;
    private Integer productId;
    private String imageUrl;
    private String altText;
    private Boolean isMain;
    private Integer displayOrder;
    private String metadata;
    private OffsetDateTime createdAt;
}