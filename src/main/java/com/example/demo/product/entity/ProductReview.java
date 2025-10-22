package com.example.demo.product.entity;

import com.example.demo.user.entity.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_reviews")
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "rating", nullable = false)
    private Integer rating; // Điểm đánh giá (ví dụ: 1 đến 5)

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp // Tự động gán thời gian khi tạo
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // Có thể thêm các trường khác như:
    // private boolean isApproved = false; // Nếu cần admin duyệt
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "order_item_id") // Liên kết với item đã mua
}