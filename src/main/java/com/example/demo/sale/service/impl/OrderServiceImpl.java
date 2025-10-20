package com.example.demo.sale.service.impl;

import com.example.demo.product.entity.ProductVariant;
import com.example.demo.product.repository.VariantRepository;
import com.example.demo.sale.dto.request.CreateOrderRequest;
import com.example.demo.sale.dto.response.OrderResponse;
import com.example.demo.sale.entity.Order;
import com.example.demo.sale.entity.OrderItem;
import com.example.demo.sale.mapper.OrderMapper;
import com.example.demo.sale.repository.OrderRepository;
import com.example.demo.sale.service.OrderService;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.user.entity.Customer;
import com.example.demo.user.repository.CustomerRepository;
import com.example.demo.user.entity.Employee;
import com.example.demo.user.repository.EmployeeRepository;
import com.example.demo.supplychain.entity.Warehouse;
import com.example.demo.supplychain.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.user.entity.User; // Import User
import com.example.demo.user.repository.UserRepository; // Import UserRepository
import org.springframework.security.core.context.SecurityContextHolder; // Import SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Import UsernameNotFoundException
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.Pageable; // Import Pageable

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final VariantRepository variantRepository;
    private final WarehouseRepository warehouseRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getMyOrders(Pageable pageable) {
        // 1. Lấy email của người dùng đang đăng nhập
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Tìm User entity từ email
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + userEmail));

        // 3. Lấy Customer ID từ User (Giả định Customer luôn tồn tại nếu User là customer)
        Integer customerId = currentUser.getCustomer() != null ? currentUser.getCustomer().getId() : null;
        if (customerId == null) {
             // Hoặc trả về trang rỗng nếu user không phải customer
            throw new BadRequestException("Tài khoản hiện tại không phải là khách hàng.");
        }

        // 4. Gọi phương thức repository
        Page<Order> orderPage = orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId, pageable);

        // 5. Map sang DTO và trả về
        return orderPage.map(orderMapper::toDto);
    }
    
    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        // Manual Validation
        if (request.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID must not be null.");
        }
        if (request.getWarehouseId() == null) {
            throw new IllegalArgumentException("Warehouse ID must not be null.");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
        for (CreateOrderRequest.ItemRequest item : request.getItems()) {
            if (item.getVariantId() == null) {
                throw new IllegalArgumentException("Item variant ID must not be null.");
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be a positive number.");
            }
        }
        
        Customer customer = customerRepository.findById(request.getCustomerId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));
        
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + request.getWarehouseId()));
        
        // Giả định một employee mặc định để xử lý đơn hàng
        Employee handledBy = employeeRepository.findById(1)
               .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Order order = orderMapper.toEntity(request);
        order.setCustomer(customer);
        order.setWarehouse(warehouse);
        order.setCreatedAt(OffsetDateTime.now());
        order.setStatus("PENDING");
        order.setHandledBy(handledBy);

        List<OrderItem> items = request.getItems().stream().map(itemDto -> {
            ProductVariant variant = variantRepository.findById(itemDto.getVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with id: " + itemDto.getVariantId()));
            
            return OrderItem.builder()
                    .order(order)
                    .variant(variant)
                    .quantity(itemDto.getQuantity())
                    .priceAtPurchase(itemDto.getUnitPrice())
                    .build();
        }).collect(Collectors.toList());

        order.setItems(items);
        
        BigDecimal grandTotal = items.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setGrandTotal(grandTotal);
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
}