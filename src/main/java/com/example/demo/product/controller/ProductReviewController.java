package com.example.demo.product.controller;

import com.example.demo.product.dto.request.CreateReviewRequest;
import com.example.demo.product.dto.response.ReviewResponse;
import com.example.demo.product.service.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews") // Tạo base path mới cho reviews
@RequiredArgsConstructor
@Tag(name = "Product Reviews", description = "API quản lý đánh giá sản phẩm")
public class ProductReviewController {

    private final ProductReviewService reviewService;

    /**
     * Lấy tất cả đánh giá cho một sản phẩm (API Public).
     */
    @GetMapping("/product/{productId}")
    @Operation(summary = "Lấy đánh giá theo ID sản phẩm (phân trang)")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByProductId(
            @PathVariable Integer productId,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        Page<ReviewResponse> reviews = reviewService.getReviewsByProductId(productId, pageable);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Tạo một đánh giá mới (API Private, yêu cầu vai trò CUSTOMER).
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Tạo một đánh giá mới (Yêu cầu role CUSTOMER)")
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {
        
        ReviewResponse newReview = reviewService.createReview(request, authentication);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }
}