package com.example.demo.product.repository;

import com.example.demo.product.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<ProductCategory, Integer> {
    // Spring Data JPA sẽ tự động tạo ra các phương thức CRUD cơ bản cho bạn
    // (findAll, findById, save, delete,...)
    // Bạn không cần viết thêm gì ở đây trừ khi có yêu cầu truy vấn đặc biệt.
}