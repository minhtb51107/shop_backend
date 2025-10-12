package com.example.demo.supplychain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.supplychain.entity.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.
    // Bạn có thể thêm các phương thức query tùy chỉnh ở đây nếu cần.
}
