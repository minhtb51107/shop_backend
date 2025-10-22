package com.example.demo.sale.repository;

import com.example.demo.sale.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <-- THÊM IMPORT NÀY
import org.springframework.data.repository.query.Param; // <-- THÊM IMPORT NÀY
import org.springframework.stereotype.Repository; // <-- THÊM IMPORT NÀY (quan trọng)

// Thêm @Repository nếu bạn chưa có
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // ========== THÊM PHƯƠNG THỨC MỚI VÀO ĐÂY ==========
    /**
     * Kiểm tra xem một người dùng (thông qua userId) đã từng đặt hàng
     * (OrderItem) chứa một sản phẩm (thông qua productId của variant) hay chưa.
     *
     * @param userId ID của User (liên kết từ Customer).
     * @param productId ID của Product (liên kết từ ProductVariant).
     * @return true nếu đã mua, false nếu chưa.
     */
    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN TRUE ELSE FALSE END " +
           "FROM OrderItem oi " +
           "JOIN oi.order o " +        // Join OrderItem với Order
           "JOIN o.customer c " +     // Join Order với Customer
           "JOIN c.user u " +         // Join Customer với User
           "JOIN oi.variant v " +     // Join OrderItem với ProductVariant
           "JOIN v.product p " +      // Join ProductVariant với Product
           "WHERE u.id = :userId AND p.id = :productId")
           // Optional: Thêm điều kiện trạng thái đơn hàng nếu cần, ví dụ:
           // AND o.status = com.example.demo.sale.entity.OrderStatus.COMPLETED
    boolean existsByOrderCustomerUserIdAndVariantProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
    // ========== KẾT THÚC THÊM PHƯƠNG THỨC ==========

    // Giữ lại các phương thức khác nếu bạn có (ví dụ: findByOrderId)
    // List<OrderItem> findByOrderId(Integer orderId);

}