package com.example.demo.sale.entity;

import com.example.demo.user.entity.Customer;
import com.example.demo.sale.enums.OrderStatus; // Đảm bảo bạn đã tạo Enum này
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders") // Đặt tên bảng là 'orders'
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status; // PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELED, RETURNED

    @Column(name = "shipping_address", nullable = false, length = 500)
    private String shippingAddress;

    @Column(name = "receiver_fullname", nullable = false)
    private String receiverFullname; // Trường mới

    @Column(name = "receiver_phone_number", nullable = false, length = 20)
    private String receiverPhoneNumber; // Trường mới

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "shipping_method", nullable = false, length = 50)
    private String shippingMethod;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "shipping_fee", precision = 10, scale = 2)
    private BigDecimal shippingFee; // Trường mới (tùy chọn)

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount; // Trường mới (tùy chọn)

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (orderDate == null) {
            orderDate = createdAt; // Mặc định ngày đặt hàng là ngày tạo
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructor, hashCode, equals nếu cần
}