package com.example.demo.finance.service.impl;

import com.example.demo.finance.dto.response.OrderSummaryResponse;
import com.example.demo.finance.dto.response.RevenueReportResponse;
import com.example.demo.finance.entity.JournalEntry;
import com.example.demo.finance.repository.JournalEntryRepository;
import com.example.demo.finance.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final JournalEntryRepository journalEntryRepository;

    @Override
    public RevenueReportResponse generateRevenueReport(int year, int month) {
        // 1. Xác định khoảng thời gian của tháng
        YearMonth yearMonth = YearMonth.of(year, month);
        OffsetDateTime startDate = yearMonth.atDay(1).atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
        OffsetDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(OffsetDateTime.now().getOffset());

        // 2. Dùng repository để lấy tất cả bút toán "ORDER" trong tháng
        List<JournalEntry> orderEntries = journalEntryRepository.findByReferenceTypeAndTransactionDateBetween("ORDER", startDate, endDate);

        // 3. Tính toán từ dữ liệu lấy được
        BigDecimal totalRevenue = BigDecimal.ZERO;
        List<OrderSummaryResponse> orderSummaries = new ArrayList<>();

        for (JournalEntry entry : orderEntries) {
            // Giả sử doanh thu là tổng credit vào tài khoản REVENUE
            BigDecimal orderRevenue = entry.getItems().stream()
                    .filter(item -> "REVENUE".equals(item.getAccount().getAccountType()))
                    .map(item -> item.getCredit())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalRevenue = totalRevenue.add(orderRevenue);

            orderSummaries.add(OrderSummaryResponse.builder()
                    .orderId(entry.getReferenceId())
                    .transactionDate(entry.getTransactionDate())
                    .description(entry.getDescription())
                    .amount(orderRevenue)
                    .build());
        }

        // 4. Xây dựng và trả về đối tượng báo cáo
        return RevenueReportResponse.builder()
                .year(year)
                .month(month)
                .totalRevenue(totalRevenue)
                .totalOrders(orderEntries.size())
                .orders(orderSummaries)
                .build();
    }
}