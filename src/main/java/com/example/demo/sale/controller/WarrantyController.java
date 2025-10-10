package com.example.demo.sale.controller;

import com.example.demo.sale.dto.request.CreateWarrantyCaseRequest;
import com.example.demo.sale.dto.response.WarrantyCaseResponse;
import com.example.demo.sale.service.WarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warranties")
@RequiredArgsConstructor
public class WarrantyController {
    private final WarrantyService warrantyService;

    @PostMapping
    public ResponseEntity<WarrantyCaseResponse> createWarrantyCase(@RequestBody CreateWarrantyCaseRequest request) {
        WarrantyCaseResponse response = warrantyService.createWarrantyCase(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}