package com.example.demo.finance.controller;

import com.example.demo.finance.dto.request.PeriodRequest;
import com.example.demo.finance.dto.response.PeriodResponse;
import com.example.demo.finance.service.FinancialPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finance/periods")
@RequiredArgsConstructor
public class FinancialPeriodController {

    private final FinancialPeriodService service;

    @PostMapping
    public ResponseEntity<PeriodResponse> createPeriod(@RequestBody PeriodRequest request) {
        return ResponseEntity.ok(service.createPeriod(request));
    }

    @GetMapping
    public ResponseEntity<List<PeriodResponse>> getAllPeriods() {
        return ResponseEntity.ok(service.getAllPeriods());
    }

    @PostMapping("/{periodId}/close")
    public ResponseEntity<PeriodResponse> closePeriod(@PathVariable Integer periodId) {
        return ResponseEntity.ok(service.closePeriod(periodId));
    }
}