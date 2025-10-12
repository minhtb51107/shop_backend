package com.example.demo.sale.service.impl;

import com.example.demo.sale.dto.request.CreateReturnRequest;
import com.example.demo.sale.dto.response.ReturnResponse;
import com.example.demo.sale.entity.Return;
import com.example.demo.sale.entity.ReturnItem;
import com.example.demo.sale.mapper.ReturnMapper;
import com.example.demo.sale.repository.OrderItemRepository;
import com.example.demo.sale.repository.OrderRepository;
import com.example.demo.sale.repository.ReturnRepository;
import com.example.demo.sale.service.ReturnService;
import com.example.demo.user.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReturnServiceImpl implements ReturnService {
    private final ReturnRepository returnRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final EmployeeRepository employeeRepository;
    private final ReturnMapper returnMapper;

    @Override
    @Transactional
    public ReturnResponse createReturn(CreateReturnRequest request) {
        // Manual Validation
        if (request.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID must not be null.");
        }
        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Reason must not be empty.");
        }
        if (request.getCreatedByEmployeeId() == null) {
            throw new IllegalArgumentException("Created By Employee ID must not be null.");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Return must contain at least one item.");
        }
        for (CreateReturnRequest.ReturnItemRequest item : request.getItems()) {
            if (item.getOrderItemId() == null) {
                throw new IllegalArgumentException("Return item's order item ID must not be null.");
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Return item quantity must be a positive number.");
            }
        }

        Return returnEntity = returnMapper.toEntity(request);
        
        returnEntity.setOrder(orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + request.getOrderId())));
        
        returnEntity.setCreatedBy(employeeRepository.findById(request.getCreatedByEmployeeId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + request.getCreatedByEmployeeId())));
        
        returnEntity.setStatus("REQUESTED");

        List<ReturnItem> items = request.getItems().stream().map(itemDto -> {
            ReturnItem item = new ReturnItem();
            item.setReturned(returnEntity);
            item.setQuantity(itemDto.getQuantity());
            item.setReason(itemDto.getReason());
            item.setOrderItem(orderItemRepository.findById(itemDto.getOrderItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Order item not found with id: " + itemDto.getOrderItemId())));
            return item;
        }).collect(Collectors.toList());

        returnEntity.setItems(items);

        Return savedReturn = returnRepository.save(returnEntity);
        return returnMapper.toDto(savedReturn);
    }

    @Override
    public ReturnResponse getReturnById(Long id) {
        Return returnEntity = returnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Return not found with id: " + id));
        return returnMapper.toDto(returnEntity);
    }

    @Override
    public List<ReturnResponse> findAllReturns() {
        return returnRepository.findAll().stream()
                .map(returnMapper::toDto)
                .collect(Collectors.toList());
    }
}