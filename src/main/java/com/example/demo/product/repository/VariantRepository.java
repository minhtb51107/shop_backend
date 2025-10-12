package com.example.demo.product.repository;

import com.example.demo.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Hãy chắc chắn rằng bạn đã import thư viện này

@Repository
public interface VariantRepository extends JpaRepository<ProductVariant, Long> {

    /**
     * Tự động tạo câu lệnh query để tìm tất cả các ProductVariant
     * dựa trên ID của Product (khóa ngoại product_id).
     * Tên phương thức phải tuân thủ quy tắc của Spring Data:
     * findBy + [Tên thuộc tính trong Entity]
     * Trong ProductVariant.java, bạn có thuộc tính "private Product product;",
     * nên Spring sẽ hiểu "ProductId" là tìm theo ID của thuộc tính "product".
     */
    List<ProductVariant> findByProductId(Integer productId);

}