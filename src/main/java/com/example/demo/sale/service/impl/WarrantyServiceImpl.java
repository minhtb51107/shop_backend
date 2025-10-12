package com.example.demo.sale.service.impl;

import com.example.demo.sale.dto.request.CreateWarrantyCaseRequest;
import com.example.demo.sale.dto.response.WarrantyCaseResponse;
import com.example.demo.sale.entity.WarrantyCase;
import com.example.demo.sale.mapper.WarrantyMapper;
import com.example.demo.sale.repository.OrderItemRepository;
import com.example.demo.sale.repository.WarrantyCaseRepository;
import com.example.demo.sale.service.WarrantyService;
import com.example.demo.user.repository.CustomerRepository;
import com.example.demo.user.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WarrantyServiceImpl implements WarrantyService {
    private final WarrantyCaseRepository warrantyCaseRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final WarrantyMapper warrantyMapper;

    @Override
    @Transactional
    public WarrantyCaseResponse createWarrantyCase(CreateWarrantyCaseRequest request) {
        WarrantyCase warrantyCase = warrantyMapper.toEntity(request);
        
        warrantyCase.setOrderItem(orderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> new EntityNotFoundException("Order item not found with id: " + request.getOrderItemId())));
        
        warrantyCase.setCustomer(customerRepository.findById(request.getCustomerId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId())));
        
        warrantyCase.setCreatedBy(employeeRepository.findById(request.getCreatedByEmployeeId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + request.getCreatedByEmployeeId())));
        
        warrantyCase.setStatus("RECEIVED");

        WarrantyCase savedCase = warrantyCaseRepository.save(warrantyCase);
        return warrantyMapper.toDto(savedCase);
    }
}