package com.example.demo.user.mapper;

import com.example.demo.product.mapper.VariantMapper;
import com.example.demo.user.dto.response.WishlistItemResponse;
import com.example.demo.user.entity.WishlistItem;
import org.springframework.stereotype.Component; // <-- Dùng @Component

import java.util.List;
import java.util.stream.Collectors;

@Component // <-- 1. Thay @Mapper bằng @Component
public class WishlistItemMapper { // <-- 2. Chuyển từ interface sang class

    // 3. Inject VariantMapper để sử dụng
    private final VariantMapper variantMapper;

    public WishlistItemMapper(VariantMapper variantMapper) {
        this.variantMapper = variantMapper;
    }

    /**
     * Chuyển từ Entity WishlistItem sang DTO WishlistItemResponse.
     * Đây là logic thủ công thay cho @Mapping.
     */
    public WishlistItemResponse toWishlistItemResponse(WishlistItem wishlistItem) {
        if (wishlistItem == null) {
            return null;
        }

        WishlistItemResponse response = new WishlistItemResponse();

        // Thay cho: @Mapping(source = "customer.id", target = "customerId")
        if (wishlistItem.getCustomer() != null) {
            response.setCustomerId(wishlistItem.getCustomer().getId());
        }

        // Thay cho: @Mapping(source = "variant", target = "variant")
        // Chúng ta gọi thủ công hàm toResponse từ VariantMapper đã inject
        if (wishlistItem.getVariant() != null) {
            response.setVariant(variantMapper.toResponse(wishlistItem.getVariant()));
        }
        
        // (Giả sử) Map các trường tên giống nhau, ví dụ: id
        // response.setId(wishlistItem.getId());

        return response;
    }

    /**
     * Chuyển từ List<Entity> sang List<DTO>.
     */
    public List<WishlistItemResponse> toWishlistItemResponseList(List<WishlistItem> wishlistItems) {
        if (wishlistItems == null) {
            return null;
        }
        
        // Dùng stream để lặp qua danh sách và gọi hàm toWishlistItemResponse ở trên
        return wishlistItems.stream()
                .map(this::toWishlistItemResponse)
                .collect(Collectors.toList());
    }
}