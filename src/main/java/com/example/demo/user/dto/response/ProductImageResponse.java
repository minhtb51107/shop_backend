package com.example.demo.user.dto.response;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class ProductImageResponse {
    private Integer id;
    private String imageUrl;
    private String altText;
    private Boolean isMain;
    private Integer displayOrder;
    private String metadata;
    private OffsetDateTime createdAt;
    private String productName;
}
