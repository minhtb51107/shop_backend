package com.example.demo.sale.repository;

import com.example.demo.sale.entity.Order;
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.Pageable; // Import Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import Query
import org.springframework.data.repository.query.Param; // Import Param

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Tìm kiếm đơn hàng của một khách hàng cụ thể, sắp xếp theo ngày tạo giảm dần.
     * Sử dụng JOIN FETCH để tải luôn thông tin customer (tùy chọn nhưng có thể hữu ích).
     * @param customerId ID của khách hàng.
     * @param pageable   Thông tin phân trang.
     * @return Page chứa danh sách đơn hàng.
     */
    @Query(value = "SELECT o FROM Order o JOIN FETCH o.customer c WHERE c.id = :customerId ORDER BY o.createdAt DESC",
           countQuery = "SELECT count(o) FROM Order o WHERE o.customer.id = :customerId")
    Page<Order> findByCustomerIdOrderByCreatedAtDesc(@Param("customerId") Integer customerId, Pageable pageable);
}