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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
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
        Return returnEntity = returnMapper.toEntity(request);
        returnEntity.setOrder(orderRepository.findById(request.getOrderId()).orElseThrow());
        returnEntity.setCreatedBy(employeeRepository.findById(request.getCreatedByEmployeeId().intValue()).orElseThrow());
        returnEntity.setStatus("REQUESTED");

        Set<ReturnItem> items = request.getItems().stream().map(itemDto -> {
            ReturnItem item = new ReturnItem();
            item.setReturnRequest(returnEntity);
            item.setQuantity(itemDto.getQuantity());
            item.setReason(itemDto.getReason());
            item.setOrderItem(orderItemRepository.findById(itemDto.getOrderItemId()).orElseThrow());
            return item;
        }).collect(Collectors.toSet());

        returnEntity.setItems(items);

        Return savedReturn = returnRepository.save(returnEntity);
        return returnMapper.toDto(savedReturn);
    }
}