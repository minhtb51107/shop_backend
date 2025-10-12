package com.example.demo.finance.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class RevenueReportResponse {
    private int year;
    private int month;
    private BigDecimal totalRevenue;
    private long totalOrders;
    private List<OrderSummaryResponse> orders;
}