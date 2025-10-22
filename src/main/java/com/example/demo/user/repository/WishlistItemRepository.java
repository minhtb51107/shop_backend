package com.example.demo.user.repository;

import com.example.demo.user.entity.WishlistItem;
import com.example.demo.user.entity.WishlistItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, WishlistItemId> {

    // Tìm tất cả các mục wishlist của một customer theo ID
    // Sử dụng LEFT JOIN FETCH để tải luôn thông tin variant và product liên quan
    @Query("SELECT wi FROM WishlistItem wi LEFT JOIN FETCH wi.variant v LEFT JOIN FETCH v.product p WHERE wi.customer.id = :customerId ORDER BY wi.createdAt DESC")
    List<WishlistItem> findByCustomerIdWithDetails(Integer customerId);

    // Tìm tất cả các mục wishlist của một customer theo ID (chỉ lấy WishlistItem)
    List<WishlistItem> findByCustomerIdOrderByCreatedAtDesc(Integer customerId);

    // Kiểm tra xem một variant đã tồn tại trong wishlist của customer chưa
    boolean existsByCustomerIdAndVariantId(Integer customerId, Long variantId);

    // Tìm một mục wishlist cụ thể
    Optional<WishlistItem> findByCustomerIdAndVariantId(Integer customerId, Long variantId);

    // Đếm số lượng item trong wishlist của customer
    long countByCustomerId(Integer customerId);
}