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
import com.example.demo.user.repository.CustomerRepository;
import com.example.demo.supplychain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final VariantRepository productVariantRepository;
    private final WarehouseRepository warehouseRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomer(customerRepository.findById(request.getCustomerId().intValue()).orElseThrow());
        order.setWarehouse(warehouseRepository.findById(request.getWarehouseId().intValue()).orElseThrow());
        order.setShippingAddress(request.getShippingAddress());
        order.setCreatedAt(OffsetDateTime.now());
        order.setStatus("PENDING");
        Set<OrderItem> items = request.getItems().stream().map(itemDto -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setQuantity(itemDto.getQuantity());

            // Lưu đối tượng ProductVariant đã tìm được vào một biến tạm thời
            ProductVariant variant = productVariantRepository.findById(itemDto.getVariantId().longValue()).orElseThrow();
            
            // Sử dụng biến tạm thời để gán cho item
            item.setVariant(variant);
            
            // Sử dụng biến tạm thời để lấy giá
            item.setPriceAtPurchase(variant.getPrice());
            
            return item;
        }).collect(Collectors.toSet());

        BigDecimal grandTotal = items.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setGrandTotal(grandTotal);
        order.setItems(items);
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(newStatus);
        orderRepository.save(order);
    }
}