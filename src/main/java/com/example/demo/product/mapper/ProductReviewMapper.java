package com.example.demo.product.mapper;

import com.example.demo.product.dto.response.ReviewResponse;
import com.example.demo.product.entity.ProductReview;
import org.springframework.stereotype.Component;

@Component
public class ProductReviewMapper {

    public ReviewResponse toResponse(ProductReview review) {
        if (review == null) {
            return null;
        }

        String customerName = "Người dùng ẩn danh";
        if (review.getCustomer() != null && review.getCustomer().getFullname() != null) {
            customerName = review.getCustomer().getFullname();
        }

        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .customerName(customerName) // Lấy tên từ customer đã fetch
                .build();
    }
}