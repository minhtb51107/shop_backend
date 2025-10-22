package com.example.demo.product.repository;

import com.example.demo.product.entity.Product;
// ----- CÁC IMPORT CẦN THIẾT -----
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification; // Import Specification
import org.springframework.data.jpa.repository.EntityGraph; // <-- THÊM IMPORT NÀY
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// ----- KHAI BÁO REPOSITORY -----
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    // ----- SỬA HÀM findAll ĐỂ LUÔN LẤY KÈM DỮ LIỆU LIÊN QUAN -----
    /**
     * Ghi đè phương thức findAll để sử dụng EntityGraph,
     * tự động JOIN FETCH các collection/entity liên quan khi truy vấn theo Specification.
     * attributePaths: Chỉ định các thuộc tính cần EAGER fetch.
     */
    @Override
    @EntityGraph(attributePaths = {"productVariants", "productImages", "category", "brand"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
    // ----- KẾT THÚC SỬA findAll -----

    // ----- THÊM PHƯƠNG THỨC MỚI ĐỂ LẤY CHI TIẾT SẢN PHẨM -----
    /**
     * Tìm Product theo ID và fetch EAGER các entity/collection liên quan cần thiết.
     * Sử dụng LEFT JOIN FETCH để đảm bảo lấy được Product ngay cả khi không có variant, image,...
     */
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.productVariants " +
           "LEFT JOIN FETCH p.productImages img " + // Thêm alias 'img' để sắp xếp được ảnh chính
           "LEFT JOIN FETCH p.brand " +
           "LEFT JOIN FETCH p.category " +
           "WHERE p.id = :id " +
           "ORDER BY img.isMain DESC") // Sắp xếp để ảnh chính (isMain=true) lên đầu nếu có
    Optional<Product> findByIdWithDetails(@Param("id") Integer id);
    // ----- KẾT THÚC THÊM findByIdWithDetails -----


    // --- HÀM TÌM SẢN PHẨM LIÊN QUAN (ĐÃ CÓ SẴN - TỐI ƯU HƠN) ---
    /**
     * Tìm các sản phẩm liên quan (cùng category, khác id, còn active).
     * Sử dụng JOIN FETCH để lấy luôn brand và category, images, variants.
     * @param categoryId ID của category.
     * @param productId ID của sản phẩm hiện tại (để loại trừ).
     * Bỏ isActive, isDeleted vì đã lọc trong Specification của Service rồi
     * @param pageable Giới hạn số lượng kết quả.
     * @return List các sản phẩm liên quan đã fetch đủ thông tin.
     */
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.productVariants " +
           "LEFT JOIN FETCH p.productImages img " + // Fetch images và sắp xếp
           "LEFT JOIN FETCH p.brand " +
           "LEFT JOIN FETCH p.category " +
           "WHERE p.category.id = :categoryId AND p.id <> :productId " +
           "ORDER BY img.isMain DESC") // Sắp xếp ảnh
    List<Product> findRelatedProductsEagerly( // Đổi tên để phân biệt với hàm cũ (nếu muốn giữ lại)
            @Param("categoryId") Integer categoryId,
            @Param("productId") Integer productId,
            Pageable pageable
    );
    // ----- KẾT THÚC HÀM TÌM SẢN PHẨM LIÊN QUAN -----

}