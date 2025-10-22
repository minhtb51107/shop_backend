package com.example.demo.user.entity;

import com.example.demo.product.entity.ProductVariant; // Import ProductVariant
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "wishlist_items")
@Getter
@Setter
@IdClass(WishlistItemId.class) // Chỉ định lớp dùng làm khóa chính kết hợp
public class WishlistItem {

    @Id
    @ManyToOne(fetch = FetchType.LAZY) // Mối quan hệ Many-to-One với Customer
    @JoinColumn(name = "customer_id", nullable = false) // Tên cột khóa ngoại trong DB
    private Customer customer;

    @Id
    @ManyToOne(fetch = FetchType.LAZY) // Mối quan hệ Many-to-One với ProductVariant
    @JoinColumn(name = "variant_id", nullable = false) // Tên cột khóa ngoại trong DB
    private ProductVariant variant;

    @CreationTimestamp // Tự động gán thời gian tạo
    @Column(name = "created_at", nullable = false, updatable = false) // updatable=false để không bị cập nhật lại
    private OffsetDateTime createdAt;

    // Constructors, equals/hashCode nếu không dùng Lombok @Data hoặc @EqualsAndHashCode
    // Lombok @Getter @Setter đã đủ dùng ở đây
}