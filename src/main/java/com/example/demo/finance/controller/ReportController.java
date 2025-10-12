package com.example.demo.finance.controller;

import com.example.demo.finance.dto.response.RevenueReportResponse;
import com.example.demo.finance.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/finance/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/revenue")
    public ResponseEntity<RevenueReportResponse> getRevenueReport(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        // Nếu không có tham số, mặc định lấy tháng/năm hiện tại
        int currentYear = (year != null) ? year : LocalDate.now().getYear();
        int currentMonth = (month != null) ? month : LocalDate.now().getMonthValue();

        RevenueReportResponse report = reportService.generateRevenueReport(currentYear, currentMonth);
        return ResponseEntity.ok(report);
    }
}