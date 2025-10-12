package com.example.demo.finance.mapper;

import com.example.demo.finance.dto.response.JournalEntryResponse;
import com.example.demo.finance.entity.JournalEntry;
import com.example.demo.finance.entity.JournalEntryItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component // Chuyển thành Spring Component
public class AccountingMapper {

    // Triển khai thủ công phương thức toJournalEntryResponse
    public JournalEntryResponse toJournalEntryResponse(JournalEntry entity) {
        if (entity == null) {
            return null;
        }

        return JournalEntryResponse.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .transactionDate(entity.getTransactionDate())
                .items(entity.getItems().stream()
                        .map(this::toJournalEntryItemResponse) // Gọi phương thức con
                        .collect(Collectors.toList()))
                .build();
    }

    // Triển khai thủ công phương thức toJournalEntryItemResponse
    public JournalEntryResponse.JournalEntryItemResponse toJournalEntryItemResponse(JournalEntryItem item) {
        if (item == null) {
            return null;
        }

        return JournalEntryResponse.JournalEntryItemResponse.builder()
                .accountCode(item.getAccount() != null ? item.getAccount().getAccountCode() : null)
                .accountName(item.getAccount() != null ? item.getAccount().getAccountName() : null)
                .debit(item.getDebit())
                .credit(item.getCredit())
                .build();
    }
}