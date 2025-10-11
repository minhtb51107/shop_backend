// File: service/SupplierService.java
package com.example.demo.product_inventory.service;

import com.example.demo.product_inventory.dto.request.SupplierRequest;
import com.example.demo.product_inventory.dto.response.SupplierResponse;
import java.util.List;
import java.util.Optional;

public interface SupplierService {
    List<SupplierResponse> findAll();
    Optional<SupplierResponse> findById(Integer id);
    SupplierResponse save(SupplierRequest request);
    // Có thể thêm update, delete sau
}