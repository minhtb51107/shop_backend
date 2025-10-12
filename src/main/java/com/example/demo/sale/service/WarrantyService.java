package com.example.demo.sale.service;

import com.example.demo.sale.dto.request.CreateWarrantyCaseRequest;
import com.example.demo.sale.dto.response.WarrantyCaseResponse;

import java.util.List;

public interface WarrantyService {
    WarrantyCaseResponse createWarrantyCase(CreateWarrantyCaseRequest request);
    WarrantyCaseResponse getWarrantyCaseById(Long id);
    List<WarrantyCaseResponse> findAllWarrantyCases();
}