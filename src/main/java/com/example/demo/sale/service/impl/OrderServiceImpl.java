package com.example.demo.sale.service.impl;

import com.example.demo.product.dto.request.UpdateStockRequest;
import com.example.demo.product.entity.ProductVariant;
import com.example.demo.product.repository.VariantRepository;
import com.example.demo.product.service.InventoryService;
import com.example.demo.sale.dto.request.CreateOrderRequest;
import com.example.demo.sale.dto.response.OrderItemResponse; // Cần thiết nếu mapper không map items
import com.example.demo.sale.dto.response.OrderResponse;
import com.example.demo.sale.entity.Order;
import com.example.demo.sale.entity.OrderItem;
import com.example.demo.sale.enums.OrderStatus;
import com.example.demo.sale.mapper.OrderItemMapper;
import com.example.demo.sale.mapper.OrderMapper;
import com.example.demo.sale.repository.OrderItemRepository;
import com.example.demo.sale.repository.OrderRepository;
import com.example.demo.sale.service.OrderService;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
// import com.example.demo.supplychain.security.CurrentUserService; // Không dùng được vì chỉ có Employee
import com.example.demo.user.entity.Customer;
import com.example.demo.user.entity.User; // Import User
import com.example.demo.user.repository.CustomerRepository;
import com.example.demo.user.repository.UserRepository; // Import UserRepository
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication; // Import Authentication
import org.springframework.security.core.context.SecurityContextHolder; // Import SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Import UsernameNotFoundException
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final VariantRepository variantRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final InventoryService inventoryService;
    // private final CurrentUserService currentUserService; // Không dùng được
    private final UserRepository userRepository; // Inject UserRepository để tìm User

    // --- Helper Function to get current User ---
    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResourceNotFoundException("User not logged in or session expired. Please log in again.");
        }
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
    }


    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        log.info("Attempting to create order for request: {}", createOrderRequest);

        // 1. Lấy thông tin User và Customer từ user đang đăng nhập
        User currentUser = getCurrentAuthenticatedUser();
        log.info("Current user ID: {}", currentUser.getId()); // Giả sử User ID là Integer

        // Tìm Customer bằng User ID (Integer)
        Customer customer = customerRepository.findByUser_Id(currentUser.getId()) // Dùng findByUser_Id(Integer)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for user ID: " + currentUser.getId() + ". Please complete your profile."));
        log.info("Customer ID found: {}", customer.getId());

        // 2. Kiểm tra tồn kho
        Long warehouseId = createOrderRequest.getWarehouseId();
        if (warehouseId == null) {
            throw new BadRequestException("Warehouse ID is missing in the order request.");
        }
        log.info("Checking stock availability for {} items in warehouse ID: {}", createOrderRequest.getItems().size(), warehouseId);
        for (CreateOrderRequest.OrderItemRequest itemRequest : createOrderRequest.getItems()) {
            boolean hasEnoughStock = inventoryService.checkStockAvailability(
                    itemRequest.getVariantId(),
                    warehouseId,
                    itemRequest.getQuantity()
            );
            if (!hasEnoughStock) {
                 ProductVariant variant = variantRepository.findById(itemRequest.getVariantId()).orElse(null);
                 String productNameInfo = "Variant ID: " + itemRequest.getVariantId();
                 if (variant != null && variant.getProduct() != null) {
                    productNameInfo = variant.getProduct().getName() + " (Variant SKU: " + variant.getSku() + ")";
                 } else if (variant != null) {
                     productNameInfo = "Unknown Product (Variant SKU: " + variant.getSku() + ")";
                 }
                 String errorMsg = "Insufficient stock for product: " + productNameInfo + ". Required quantity: " + itemRequest.getQuantity();
                 log.error("Order creation failed: {}", errorMsg);
                 throw new BadRequestException(errorMsg);
            }
             log.debug("Stock available for variant ID: {}", itemRequest.getVariantId());
        }
        log.info("Stock availability check passed.");

        // 3. Tạo đối tượng Order (Sử dụng cấu trúc Order entity mới nhất bạn cung cấp)
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(createOrderRequest.getShippingAddress());
        order.setNotes(createOrderRequest.getNotes());
        order.setShippingMethod(createOrderRequest.getShippingMethod());
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());
        order.setReceiverFullname(createOrderRequest.getReceiverFullname());
        order.setReceiverPhoneNumber(createOrderRequest.getReceiverPhoneNumber());
        order.setShippingFee(createOrderRequest.getShippingFee() != null ? createOrderRequest.getShippingFee() : BigDecimal.ZERO);
        order.setDiscountAmount(createOrderRequest.getDiscountAmount() != null ? createOrderRequest.getDiscountAmount() : BigDecimal.ZERO);

        log.info("Order object created with PENDING status.");

        // 4. Tạo danh sách OrderItem và tính tổng tiền các item
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal itemsTotalAmount = BigDecimal.ZERO;

        log.info("Processing order items...");
        for (CreateOrderRequest.OrderItemRequest itemRequest : createOrderRequest.getItems()) {
            ProductVariant variant = variantRepository.findById(itemRequest.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product Variant not found with id: " + itemRequest.getVariantId()));

            OrderItem orderItem = new OrderItem();
            // KHÔNG set Order vội
            orderItem.setVariant(variant); // Dùng setVariant() khớp với OrderItem entity
            orderItem.setQuantity(itemRequest.getQuantity());
            // Lấy giá bán hiện tại từ variant.getPrice() và gán vào priceAtPurchase
            orderItem.setPriceAtPurchase(variant.getPrice()); // Dùng variant.getPrice() và setPriceAtPurchase()

            // Tính tổng tiền cho item này (dựa trên priceAtPurchase)
            BigDecimal itemTotalPrice = orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            orderItems.add(orderItem);
            itemsTotalAmount = itemsTotalAmount.add(itemTotalPrice); // Cộng dồn tổng tiền item
            log.debug("Processed item - Variant ID: {}, Qty: {}, PriceAtPurchase: {}, ItemTotal: {}",
                     variant.getId(), orderItem.getQuantity(), orderItem.getPriceAtPurchase(), itemTotalPrice);
        }
        log.info("Total amount for items calculated: {}", itemsTotalAmount);

        // 5. Tính tổng tiền cuối cùng cho Order
        BigDecimal finalTotalAmount = itemsTotalAmount.add(order.getShippingFee()).subtract(order.getDiscountAmount());
        if (finalTotalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalTotalAmount = BigDecimal.ZERO;
        }
        order.setTotalAmount(finalTotalAmount); // Dùng setTotalAmount() khớp với Order entity mới
        log.info("Final total amount calculated: {}", finalTotalAmount);

        // 6. Lưu Order vào database (để lấy ID)
        log.info("Saving order to database...");
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved successfully with ID: {}", savedOrder.getId());

        // 7. Gán ID của Order đã lưu cho từng OrderItem và lưu OrderItem vào database
        log.info("Setting order reference for items and saving items...");
        final Long savedOrderId = savedOrder.getId();
        for(OrderItem item : orderItems) {
            item.setOrder(savedOrder); // Cập nhật tham chiếu Order
        }
        List<OrderItem> savedItems = orderItemRepository.saveAll(orderItems); // Lưu tất cả OrderItem
        // Cập nhật lại list items trong Order object
        if (savedOrder.getItems() == null) savedOrder.setItems(new ArrayList<>());
        savedOrder.getItems().clear();
        savedOrder.getItems().addAll(savedItems);
        log.info("Saved {} order items.", savedItems.size());

        // 8. Cập nhật (giảm) số lượng tồn kho cho từng sản phẩm
        log.info("Updating inventory stock...");
        try {
            for (CreateOrderRequest.OrderItemRequest itemRequest : createOrderRequest.getItems()) {
                UpdateStockRequest stockRequest = new UpdateStockRequest();
                stockRequest.setVariantId(itemRequest.getVariantId());
                stockRequest.setWarehouseId(warehouseId.intValue()); // Chuyển sang Integer
                stockRequest.setQuantityChange(itemRequest.getQuantity());
                stockRequest.setTransactionType("SALE");
                stockRequest.setReferenceId(savedOrderId.toString());

                inventoryService.updateStock(stockRequest);
                 log.debug("Stock updated for Variant ID: {} at Warehouse ID: {} by quantity: -{}",
                          itemRequest.getVariantId(), stockRequest.getWarehouseId(), itemRequest.getQuantity());
            }
             log.info("Inventory stock updated successfully.");
        } catch (Exception e) {
             log.error("Error updating inventory stock after order creation for order ID: {}. Transaction will be rolled back. Error: {}", savedOrderId, e.getMessage(), e);
             throw new RuntimeException("Failed to update inventory stock after order creation. Order has been rolled back.", e);
        }

        // 9. Map Order đã lưu sang OrderResponse để trả về
        log.info("Mapping saved order to response DTO...");
        // Gọi phương thức mapper toDto khớp với tên bạn đặt
        OrderResponse response = orderMapper.toDto(savedOrder);

        // Map items thủ công nếu mapper không làm (dùng toDto của OrderItemMapper)
        if (response.getItems() == null || response.getItems().isEmpty()) {
             response.setItems(savedItems.stream()
                                  .map(orderItemMapper::toDto) // Gọi toDto
                                  .collect(Collectors.toList()));
             log.warn("OrderMapper did not map items automatically, mapped manually.");
         }

        log.info("Order created successfully. Order ID: {}", response.getId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getMyOrders(Pageable pageable) {
        User currentUser = getCurrentAuthenticatedUser();

        Customer customer = customerRepository.findByUser_Id(currentUser.getId()) // Dùng findByUser_Id(Integer)
                .orElseThrow(() -> new BadRequestException("Current account is not a customer account."));
        Integer customerIdInt = customer.getId(); // ID của Customer là Integer

        // Gọi phương thức repository với Integer customerId
        Page<Order> orderPage = orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerIdInt, pageable);

        // Map sang DTO (dùng toDto)
        return orderPage.map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                // TODO: Đảm bảo fetch items (EAGER hoặc JOIN FETCH)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // TODO: Thêm logic kiểm tra quyền của user hiện tại
        // Ví dụ: Lấy user hiện tại và so sánh customer ID
        // User currentUser = getCurrentAuthenticatedUser();
        // if (!isAdmin(currentUser) && !order.getCustomer().getUser().getId().equals(currentUser.getId())) {
        //     throw new AccessDeniedException("You do not have permission to view this order.");
        // }

        return orderMapper.toDto(order); // Dùng toDto
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String newStatusString) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        try {
            OrderStatus newStatus = OrderStatus.valueOf(newStatusString.toUpperCase());
            // TODO: Thêm logic kiểm tra chuyển trạng thái hợp lệ
            order.setStatus(newStatus);
            orderRepository.save(order);
            log.info("Order status updated for Order ID: {} to {}", orderId, newStatus);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status string provided: {}", newStatusString);
            throw new BadRequestException("Invalid order status value: " + newStatusString);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        // Chỉ nên dùng cho Admin
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto) // Dùng toDto
                .collect(Collectors.toList());
    }
}