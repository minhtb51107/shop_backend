package com.example.demo.finance.service.impl;

import com.example.demo.finance.dto.request.JournalEntryRequest;
import com.example.demo.finance.dto.response.JournalEntryResponse;
import com.example.demo.finance.entity.ChartOfAccounts;
import com.example.demo.finance.entity.FinancialPeriod;
import com.example.demo.finance.entity.JournalEntry;
import com.example.demo.finance.entity.JournalEntryItem;
import com.example.demo.finance.mapper.AccountingMapper;
import com.example.demo.finance.repository.ChartOfAccountsRepository;
import com.example.demo.finance.repository.FinancialPeriodRepository;
import com.example.demo.finance.repository.JournalEntryRepository;
import com.example.demo.finance.service.AccountingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService {

    private final JournalEntryRepository journalEntryRepository;
    private final FinancialPeriodRepository financialPeriodRepository;
    private final ChartOfAccountsRepository chartOfAccountsRepository;
    private final AccountingMapper mapper; // Inject mapper vào

    @Override
    @Transactional
    public JournalEntryResponse createJournalEntry(JournalEntryRequest request) {
        //  Validation: Tổng Nợ phải bằng Tổng Có
        BigDecimal totalDebit = request.getItems().stream()
                .map(item -> item.getDebit() != null ? item.getDebit() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredit = request.getItems().stream()
                .map(item -> item.getCredit() != null ? item.getCredit() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalArgumentException("Total debit must equal total credit.");
        }

        //  Lấy các đối tượng liên quan từ DB
        FinancialPeriod period = financialPeriodRepository.findById(request.getPeriodId())
                .orElseThrow(() -> new RuntimeException("Financial period not found with id: " + request.getPeriodId()));

        //  Tạo đối tượng JournalEntry chính
        JournalEntry entry = JournalEntry.builder()
                .period(period)
                .description(request.getDescription())
                .referenceType(request.getReferenceType())
                .referenceId(request.getReferenceId())
                .transactionDate(OffsetDateTime.now())
                .items(new ArrayList<>())
                .build();

        //  Tạo các đối tượng JournalEntryItem con
        for (JournalEntryRequest.JournalEntryItemRequest itemRequest : request.getItems()) {
            ChartOfAccounts account = chartOfAccountsRepository.findById(itemRequest.getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found with id: " + itemRequest.getAccountId()));

            JournalEntryItem item = JournalEntryItem.builder()
                    .entry(entry) // Liên kết với bút toán cha
                    .account(account)
                    .debit(itemRequest.getDebit() != null ? itemRequest.getDebit() : BigDecimal.ZERO)
                    .credit(itemRequest.getCredit() != null ? itemRequest.getCredit() : BigDecimal.ZERO)
                    .build();
            entry.getItems().add(item);
        }

        // Lưu vào DB
        JournalEntry savedEntry = journalEntryRepository.save(entry);

        //  Dùng mapper để chuyển đổi sang DTO trả về
        return mapper.toJournalEntryResponse(savedEntry);
    }
}