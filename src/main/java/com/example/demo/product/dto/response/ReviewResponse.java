package com.example.demo.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder // Giữ @Builder để dùng trong Service
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private OffsetDateTime createdAt;
    private String customerName;
    private boolean verifiedPurchase; // <-- THÊM TRƯỜNG NÀY
}