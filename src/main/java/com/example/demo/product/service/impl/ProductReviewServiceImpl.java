package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.CreateReviewRequest;
import com.example.demo.product.dto.response.ReviewResponse;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.ProductReview;
import com.example.demo.product.mapper.ProductReviewMapper;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.repository.ProductReviewRepository;
import com.example.demo.product.service.ProductReviewService;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import com.example.demo.user.entity.Customer;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // <-- THÊM IMPORT
import java.util.stream.Collectors; // <-- THÊM IMPORT
import org.springframework.data.domain.PageImpl; // <-- THÊM IMPORT
import com.example.demo.sale.repository.OrderItemRepository; // <-- THÊM IMPORT

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductReviewMapper reviewMapper;
    private final OrderItemRepository orderItemRepository; // <-- INJECT REPO ĐƠN HÀNG

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByProductId(Integer productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productId);
        }
        // Lấy Page<ProductReview> từ repo (đã fetch customer)
        Page<ProductReview> reviewPage = reviewRepository.findByProductIdWithCustomer(productId, pageable);

        // Map sang List<ReviewResponse> và kiểm tra mua hàng
        List<ReviewResponse> responseList = reviewPage.getContent().stream()
                .map(review -> {
                    // Gọi mapper cũ
                    ReviewResponse response = reviewMapper.toResponse(review);

                    // Kiểm tra xem customer này đã mua sản phẩm này chưa
                    boolean hasPurchased = orderItemRepository.existsByOrderCustomerUserIdAndVariantProductId(
                            review.getCustomer().getUser().getId(), // Lấy UserId từ Customer
                            productId
                    );
                    response.setVerifiedPurchase(hasPurchased); // Gán cờ

                    return response;
                })
                .collect(Collectors.toList());

        // Tạo lại đối tượng Page với List<ReviewResponse> đã xử lý
        return new PageImpl<>(responseList, pageable, reviewPage.getTotalElements());
    }

    @Override
    @Transactional
    public ReviewResponse createReview(CreateReviewRequest request, Authentication authentication) {
        // ... (Logic lấy customer, kiểm tra product, kiểm tra đã review giữ nguyên) ...
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + userEmail));
        Customer customer = user.getCustomer();
        if (customer == null) {
            throw new BadRequestException("Chỉ khách hàng mới có thể đánh giá.");
        }
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + request.getProductId()));
        if (reviewRepository.existsByProductIdAndCustomerId(product.getId(), customer.getId())) {
            throw new BadRequestException("Bạn đã đánh giá sản phẩm này rồi.");
        }

        // Tạo và lưu đánh giá mới
        ProductReview review = ProductReview.builder()
                .product(product)
                .customer(customer)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        ProductReview savedReview = reviewRepository.save(review);

        // Kiểm tra mua hàng để gắn cờ cho response trả về ngay
        boolean hasPurchased = orderItemRepository.existsByOrderCustomerUserIdAndVariantProductId(
                user.getId(), // Dùng UserId
                product.getId()
        );

        // Map và trả về (có kèm cờ verifiedPurchase)
        return ReviewResponse.builder()
                .id(savedReview.getId())
                .rating(savedReview.getRating())
                .comment(savedReview.getComment())
                .createdAt(savedReview.getCreatedAt())
                .customerName(customer.getFullname())
                .verifiedPurchase(hasPurchased) // <-- GÁN CỜ
                .build();
    }
}