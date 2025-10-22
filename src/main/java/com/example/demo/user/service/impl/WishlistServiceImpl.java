package com.example.demo.user.service.impl;

import com.example.demo.product.entity.ProductVariant;
import com.example.demo.product.repository.VariantRepository; // Import VariantRepository
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import com.example.demo.user.dto.response.WishlistItemResponse;
import com.example.demo.user.entity.Customer;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.WishlistItem;
import com.example.demo.user.entity.WishlistItemId;
import com.example.demo.user.mapper.WishlistItemMapper; // Sẽ tạo Mapper này sau
import com.example.demo.user.repository.CustomerRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.WishlistItemRepository;
import com.example.demo.user.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final CustomerRepository customerRepository;
    private final VariantRepository variantRepository; // Cần để kiểm tra variant tồn tại
    private final UserRepository userRepository;       // Cần để lấy customer từ user
    private final WishlistItemMapper wishlistItemMapper; // Cần để chuyển đổi sang DTO

    // Hàm tiện ích để lấy Customer từ Authentication
    private Customer getCurrentCustomer(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("Yêu cầu đăng nhập để thực hiện thao tác này.");
        }
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + userEmail));
        Customer customer = user.getCustomer();
        if (customer == null) {
            // Trường hợp user đăng nhập là employee hoặc admin thì không có customer
             throw new BadRequestException("Chỉ khách hàng mới có danh sách yêu thích.");
        }
        return customer;
    }

    @Override
    @Transactional(readOnly = true) // Tối ưu cho việc đọc
    public List<WishlistItemResponse> getMyWishlist(Authentication authentication) {
        Customer customer = getCurrentCustomer(authentication);
        // Dùng phương thức đã tạo trong Repository để fetch kèm details
        List<WishlistItem> items = wishlistItemRepository.findByCustomerIdWithDetails(customer.getId());
        // Chuyển đổi List<WishlistItem> sang List<WishlistItemResponse> bằng Mapper
        return wishlistItemMapper.toWishlistItemResponseList(items);
    }

     @Override
     @Transactional(readOnly = true)
     public List<Long> getMyWishlistVariantIds(Authentication authentication) {
         Customer customer = getCurrentCustomer(authentication);
         List<WishlistItem> items = wishlistItemRepository.findByCustomerIdOrderByCreatedAtDesc(customer.getId());
         return items.stream()
                     .map(item -> item.getVariant().getId())
                     .collect(Collectors.toList());
     }


    @Override
    public void addToWishlist(Long variantId, Authentication authentication) {
        Customer customer = getCurrentCustomer(authentication);
        Integer customerId = customer.getId();

        // Kiểm tra xem variant có tồn tại không
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm (biến thể) không tồn tại với ID: " + variantId));

        // Kiểm tra xem đã có trong wishlist chưa
        if (wishlistItemRepository.existsByCustomerIdAndVariantId(customerId, variantId)) {
            // Có thể không cần báo lỗi, chỉ đơn giản là không làm gì cả
            // throw new BadRequestException("Sản phẩm này đã có trong danh sách yêu thích của bạn.");
             System.out.println("Variant " + variantId + " already in wishlist for customer " + customerId);
             return; // Thoát nếu đã tồn tại
        }

        // Tạo mới WishlistItem
        WishlistItem newItem = new WishlistItem();
        newItem.setCustomer(customer);
        newItem.setVariant(variant);
        // createdAt sẽ tự động được gán bởi @CreationTimestamp

        wishlistItemRepository.save(newItem);
    }

    @Override
    public void removeFromWishlist(Long variantId, Authentication authentication) {
        Customer customer = getCurrentCustomer(authentication);
        Integer customerId = customer.getId();

        // Tìm item cần xóa
        WishlistItem itemToRemove = wishlistItemRepository.findByCustomerIdAndVariantId(customerId, variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không có trong danh sách yêu thích của bạn."));

        wishlistItemRepository.delete(itemToRemove);
    }
}