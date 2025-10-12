package com.example.demo.finance.service;

import com.example.demo.finance.dto.response.RevenueReportResponse;

public interface ReportService {
    RevenueReportResponse generateRevenueReport(int year, int month);
}