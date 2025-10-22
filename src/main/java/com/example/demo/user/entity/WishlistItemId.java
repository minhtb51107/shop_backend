package com.example.demo.user.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp định danh khóa chính kết hợp cho WishlistItem.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemId implements Serializable {

    private static final long serialVersionUID = 1L; // Cần thiết cho Serializable

    private Integer customer; // Tên trường phải khớp với tên thuộc tính trong WishlistItem trỏ đến Customer
    private Long variant;    // Tên trường phải khớp với tên thuộc tính trong WishlistItem trỏ đến ProductVariant

    // Cần override equals() và hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistItemId that = (WishlistItemId) o;
        return Objects.equals(customer, that.customer) && Objects.equals(variant, that.variant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, variant);
    }
}