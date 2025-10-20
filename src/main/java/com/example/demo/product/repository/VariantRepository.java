// src/main/java/com/example/demo/product/repository/VariantRepository.java
package com.example.demo.product.repository;

import com.example.demo.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <<< THÊM IMPORT
import org.springframework.data.repository.query.Param; // <<< THÊM IMPORT
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Thêm @Repository nếu chưa có
public interface VariantRepository extends JpaRepository<ProductVariant, Long> { // ID của Variant là Long

    // Sử dụng @Query và @Param thay vì dựa vào tên phương thức
    // Giả sử trong ProductVariant entity có thuộc tính "Product product"
	@Query("SELECT pv FROM ProductVariant pv WHERE pv.product.id = :prodId") // Đổi thành :prodId
	List<ProductVariant> findByProductId(@Param("prodId") Integer productId); // Đổi thành @Param("prodId")

    // HOẶC nếu trong ProductVariant entity có thuộc tính "Integer productId" trực tiếp
    // @Query("SELECT pv FROM ProductVariant pv WHERE pv.productId = :pid")
    // List<ProductVariant> findByProductId(@Param("pid") Integer productId);

}