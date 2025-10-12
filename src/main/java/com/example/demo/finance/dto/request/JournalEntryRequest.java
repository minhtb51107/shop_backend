package com.example.demo.finance.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class JournalEntryRequest {
    private Integer periodId;
    private String description;
    private String referenceType;
    private Long referenceId;
    private List<JournalEntryItemRequest> items;

    // Class con để chứa thông tin của mỗi dòng
    @Data
    public static class JournalEntryItemRequest {
        private Integer accountId;
        private BigDecimal debit;
        private BigDecimal credit;
    }
}