package com.example.demo.finance.service;

import com.example.demo.finance.dto.request.JournalEntryRequest;
import com.example.demo.finance.dto.response.JournalEntryResponse;

public interface AccountingService {
    JournalEntryResponse createJournalEntry(JournalEntryRequest request);
}