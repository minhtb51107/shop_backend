package com.example.demo.sale.controller;

import com.example.demo.sale.dto.request.CreateWarrantyCaseRequest;
import com.example.demo.sale.dto.response.WarrantyCaseResponse;
import com.example.demo.sale.service.WarrantyService; // Giả định bạn có WarrantyService
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/warranty")
@RequiredArgsConstructor
public class WarrantyController {
    private final WarrantyService warrantyService;

    @PostMapping
    public ResponseEntity<WarrantyCaseResponse> createWarrantyCase(@RequestBody CreateWarrantyCaseRequest request) {
        WarrantyCaseResponse createdCase = warrantyService.createWarrantyCase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarrantyCaseResponse> getWarrantyCaseById(@PathVariable Long id) {
        WarrantyCaseResponse warrantyCaseResponse = warrantyService.getWarrantyCaseById(id);
        return ResponseEntity.ok(warrantyCaseResponse);
    }
    
    @GetMapping
    public ResponseEntity<List<WarrantyCaseResponse>> getAllWarrantyCases() {
        List<WarrantyCaseResponse> cases = warrantyService.findAllWarrantyCases();
        return ResponseEntity.ok(cases);
    }
}