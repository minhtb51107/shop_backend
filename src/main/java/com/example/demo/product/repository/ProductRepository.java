package com.example.demo.product.repository;

import com.example.demo.product.entity.Product;
import org.springframework.data.domain.Pageable; // Import Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    // ... các phương thức cũ ...

    // --- THÊM PHƯƠNG THỨC MỚI ---
    /**
     * Tìm các sản phẩm liên quan (cùng category, khác id, còn active).
     * @param categoryId ID của category.
     * @param productId ID của sản phẩm hiện tại (để loại trừ).
     * @param pageable Giới hạn số lượng kết quả (ví dụ: lấy 4 sản phẩm).
     * @return List các sản phẩm liên quan.
     */
    @Query("SELECT p FROM Product p JOIN FETCH p.brand JOIN FETCH p.category " +
           "WHERE p.category.id = :categoryId AND p.id <> :productId AND p.isActive = true AND p.isDeleted = false")
    List<Product> findRelatedProducts(
            @Param("categoryId") Integer categoryId,
            @Param("productId") Integer productId,
            Pageable pageable
    );
}