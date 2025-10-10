package com.example.demo;

import com.example.demo.finance.dto.request.JournalEntryRequest;
import com.example.demo.finance.entity.ChartOfAccounts;
import com.example.demo.finance.entity.FinancialPeriod;
import com.example.demo.finance.repository.ChartOfAccountsRepository;
import com.example.demo.finance.repository.FinancialPeriodRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountingControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ChartOfAccountsRepository chartOfAccountsRepository;
    private final FinancialPeriodRepository financialPeriodRepository;

    @Autowired
    public AccountingControllerTest(MockMvc mockMvc,
                                    ObjectMapper objectMapper,
                                    ChartOfAccountsRepository chartOfAccountsRepository,
                                    FinancialPeriodRepository financialPeriodRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.chartOfAccountsRepository = chartOfAccountsRepository;
        this.financialPeriodRepository = financialPeriodRepository;
    }

    private ChartOfAccounts cashAccount;
    private ChartOfAccounts revenueAccount;
    private FinancialPeriod currentPeriod;

    @BeforeEach
    void setup() {
        // Xóa dữ liệu cũ để đảm bảo test độc lập
        chartOfAccountsRepository.deleteAll();
        financialPeriodRepository.deleteAll();

        // Tạo dữ liệu nền cần thiết cho bài test
        cashAccount = chartOfAccountsRepository.save(new ChartOfAccounts(null, "111", "Tiền mặt", "ASSET", true, null));
        revenueAccount = chartOfAccountsRepository.save(new ChartOfAccounts(null, "511", "Doanh thu", "REVENUE", true, null));
        currentPeriod = financialPeriodRepository.save(new FinancialPeriod(null, "Tháng 10, 2025", LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31), "OPEN"));
    }

    @Test
    void shouldCreateJournalEntry_whenRequestIsValid() throws Exception {
        // 1. Chuẩn bị đối tượng request DTO
        JournalEntryRequest.JournalEntryItemRequest debitItem = new JournalEntryRequest.JournalEntryItemRequest();
        debitItem.setAccountId(cashAccount.getId());
        debitItem.setDebit(new BigDecimal("1500.00"));

        JournalEntryRequest.JournalEntryItemRequest creditItem = new JournalEntryRequest.JournalEntryItemRequest();
        creditItem.setAccountId(revenueAccount.getId());
        creditItem.setCredit(new BigDecimal("1500.00"));

        JournalEntryRequest request = new JournalEntryRequest();
        request.setPeriodId(currentPeriod.getId());
        request.setDescription("Test sale transaction");
        request.setItems(List.of(debitItem, creditItem));

        // 2. "Driver" thực thi: Gọi đến API và kiểm tra kết quả
        mockMvc.perform(post("/finance/journal-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Mong đợi status 200 OK
                .andExpect(jsonPath("$.id").exists()) // Mong đợi có trường "id" trong JSON trả về
                .andExpect(jsonPath("$.description").value("Test sale transaction"))
                .andExpect(jsonPath("$.items.length()").value(2));
    }
}