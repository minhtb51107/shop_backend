package com.example.demo.user.controller;

import com.example.demo.user.dto.response.WishlistItemResponse;
import com.example.demo.user.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist") // Base path cho tất cả API wishlist
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "API quản lý danh sách yêu thích của khách hàng")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực JWT cho tất cả API trong controller này
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/my") // Path: /api/v1/wishlist/my
    @PreAuthorize("hasRole('CUSTOMER')") // Chỉ customer mới được truy cập
    @Operation(summary = "Lấy danh sách yêu thích của tôi",
               description = "Trả về danh sách các sản phẩm (biến thể) trong danh sách yêu thích của người dùng đang đăng nhập.")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @ApiResponse(responseCode = "401", description = "Chưa xác thực")
    @ApiResponse(responseCode = "403", description = "Không có quyền (không phải customer)")
    public ResponseEntity<List<WishlistItemResponse>> getMyWishlist(Authentication authentication) {
        List<WishlistItemResponse> wishlist = wishlistService.getMyWishlist(authentication);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/my/ids") // Path: /api/v1/wishlist/my/ids
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Lấy danh sách ID variant yêu thích của tôi",
               description = "Trả về danh sách chỉ gồm các ID của variant trong wishlist. Dùng để kiểm tra nhanh trạng thái yêu thích trên frontend.")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách ID thành công")
    public ResponseEntity<List<Long>> getMyWishlistVariantIds(Authentication authentication) {
        List<Long> variantIds = wishlistService.getMyWishlistVariantIds(authentication);
        return ResponseEntity.ok(variantIds);
    }


    @PostMapping("/{variantId}") // Path: /api/v1/wishlist/{variantId}
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Thêm sản phẩm vào danh sách yêu thích",
               description = "Thêm một biến thể sản phẩm vào danh sách yêu thích của người dùng đang đăng nhập.")
    @ApiResponse(responseCode = "201", description = "Thêm thành công") // 201 Created thường phù hợp hơn 200 OK cho POST
    @ApiResponse(responseCode = "400", description = "Sản phẩm đã tồn tại trong wishlist hoặc không tìm thấy sản phẩm")
    @ApiResponse(responseCode = "401", description = "Chưa xác thực")
    @ApiResponse(responseCode = "403", description = "Không có quyền")
    public ResponseEntity<Void> addItemToWishlist(@PathVariable Long variantId, Authentication authentication) {
        wishlistService.addToWishlist(variantId, authentication);
        // Trả về 201 Created thay vì 200 OK
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{variantId}") // Path: /api/v1/wishlist/{variantId}
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Xóa sản phẩm khỏi danh sách yêu thích",
               description = "Xóa một biến thể sản phẩm khỏi danh sách yêu thích của người dùng đang đăng nhập.")
    @ApiResponse(responseCode = "204", description = "Xóa thành công") // 204 No Content là chuẩn cho DELETE thành công
    @ApiResponse(responseCode = "404", description = "Sản phẩm không tìm thấy trong wishlist")
    @ApiResponse(responseCode = "401", description = "Chưa xác thực")
    @ApiResponse(responseCode = "403", description = "Không có quyền")
    public ResponseEntity<Void> removeItemFromWishlist(@PathVariable Long variantId, Authentication authentication) {
        wishlistService.removeFromWishlist(variantId, authentication);
        // Trả về 204 No Content
        return ResponseEntity.noContent().build();
    }
}