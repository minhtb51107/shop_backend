package com.example.demo.finance.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class JournalEntryResponse {
    private Long id;
    private String description;
    private OffsetDateTime transactionDate;
    private List<JournalEntryItemResponse> items;

    @Data
    @Builder
    public static class JournalEntryItemResponse {
        private String accountCode;
        private String accountName;
        private BigDecimal debit;
        private BigDecimal credit;
    }
}