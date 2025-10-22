package com.example.demo.user.dto.response;

import com.example.demo.product.dto.response.VariantResponse; // Import DTO của Variant

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * DTO trả về thông tin chi tiết của một mục trong danh sách yêu thích.
 */
@Getter
@Setter
@Builder // Dùng Builder pattern để dễ tạo đối tượng
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemResponse {

    private Integer customerId; // Chỉ cần ID của customer
    private VariantResponse variant; // Trả về thông tin chi tiết của variant (bao gồm cả product)
    private OffsetDateTime createdAt;

    // Không cần trường variantId riêng vì nó đã có trong VariantResponse
}