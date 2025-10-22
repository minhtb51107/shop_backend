package com.example.demo.product.repository;

import com.example.demo.product.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    /**
     * Lấy tất cả đánh giá cho một sản phẩm (phân trang và sắp xếp mới nhất trước).
     * Sử dụng JOIN FETCH để lấy luôn thông tin customer (để lấy tên).
     */
    @Query(value = "SELECT r FROM ProductReview r LEFT JOIN FETCH r.customer c " +
                   "WHERE r.product.id = :productId",
           countQuery = "SELECT count(r) FROM ProductReview r WHERE r.product.id = :productId")
    Page<ProductReview> findByProductIdWithCustomer(Integer productId, Pageable pageable);

    // (Tùy chọn) Kiểm tra xem khách hàng đã đánh giá sản phẩm này chưa
    boolean existsByProductIdAndCustomerId(Integer productId, Integer customerId);
}