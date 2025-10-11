// File: service/impl/SupplierServiceImpl.java
package com.example.demo.product_inventory.service.impl;

import com.example.demo.product_inventory.dto.request.SupplierRequest;
import com.example.demo.product_inventory.dto.response.SupplierResponse;
import com.example.demo.product_inventory.entity.Supplier;
import com.example.demo.product_inventory.mapper.SupplierMapper;
import com.example.demo.product_inventory.repository.SupplierRepository;
import com.example.demo.product_inventory.service.SupplierService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public List<SupplierResponse> findAll() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SupplierResponse> findById(Integer id) {
        return supplierRepository.findById(id).map(supplierMapper::toResponse);
    }

    @Override
    public SupplierResponse save(SupplierRequest request) {
        Supplier supplier = supplierMapper.toEntity(request);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toResponse(savedSupplier);
    }
}