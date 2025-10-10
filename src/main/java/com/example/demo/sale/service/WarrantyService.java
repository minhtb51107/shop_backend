package com.example.demo.sale.service;

import com.example.demo.sale.dto.request.CreateWarrantyCaseRequest;
import com.example.demo.sale.dto.response.WarrantyCaseResponse;

public interface WarrantyService {
    WarrantyCaseResponse createWarrantyCase(CreateWarrantyCaseRequest request);
}