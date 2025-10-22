package com.example.demo.user.service;

import com.example.demo.user.dto.response.WishlistItemResponse; // Sẽ tạo DTO này sau
import org.springframework.security.core.Authentication;

import java.util.List;

public interface WishlistService {

    /**
     * Lấy danh sách sản phẩm yêu thích của người dùng hiện tại.
     * @param authentication Đối tượng chứa thông tin người dùng đang đăng nhập.
     * @return Danh sách các mục trong wishlist.
     */
    List<WishlistItemResponse> getMyWishlist(Authentication authentication);

    /**
     * Thêm một biến thể sản phẩm vào danh sách yêu thích của người dùng hiện tại.
     * @param variantId ID của biến thể sản phẩm cần thêm.
     * @param authentication Đối tượng chứa thông tin người dùng đang đăng nhập.
     */
    void addToWishlist(Long variantId, Authentication authentication);

    /**
     * Xóa một biến thể sản phẩm khỏi danh sách yêu thích của người dùng hiện tại.
     * @param variantId ID của biến thể sản phẩm cần xóa.
     * @param authentication Đối tượng chứa thông tin người dùng đang đăng nhập.
     */
    void removeFromWishlist(Long variantId, Authentication authentication);

    /**
     * Lấy danh sách ID các biến thể sản phẩm có trong wishlist của người dùng hiện tại.
     * Dùng để kiểm tra nhanh trạng thái "yêu thích" trên frontend.
     * @param authentication Đối tượng chứa thông tin người dùng đang đăng nhập.
     * @return Danh sách ID các variant trong wishlist.
     */
    List<Long> getMyWishlistVariantIds(Authentication authentication);
}