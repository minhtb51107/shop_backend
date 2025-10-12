package com.example.demo.sale.service.impl;

import com.example.demo.sale.dto.response.OrderStatusHistoryResponse;
import com.example.demo.sale.mapper.OrderStatusHistoryMapper;
import com.example.demo.sale.repository.OrderStatusHistoryRepository;
import com.example.demo.sale.service.OrderStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService {
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final OrderStatusHistoryMapper statusHistoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatusHistoryResponse> getHistoryByOrderId(Long orderId) {
        return statusHistoryRepository.findByOrderId(orderId).stream()
                .map(statusHistoryMapper::toDto)
                .collect(Collectors.toList());
    }
}