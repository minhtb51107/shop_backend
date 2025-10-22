package com.example.demo.product.service;

import com.example.demo.product.dto.request.CreateReviewRequest;
import com.example.demo.product.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ProductReviewService {
    /**
     * Lấy đánh giá của một sản phẩm (có phân trang).
     */
    Page<ReviewResponse> getReviewsByProductId(Integer productId, Pageable pageable);

    /**
     * Tạo một đánh giá mới.
     */
    ReviewResponse createReview(CreateReviewRequest request, Authentication authentication);
}