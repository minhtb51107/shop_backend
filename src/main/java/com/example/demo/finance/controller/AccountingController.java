package com.example.demo.finance.controller;

import com.example.demo.finance.dto.request.JournalEntryRequest;
import com.example.demo.finance.dto.response.JournalEntryResponse;
import com.example.demo.finance.service.AccountingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("finance/journal-entries")
@RequiredArgsConstructor
public class AccountingController {

    private final AccountingService accountingService;

    @PostMapping
    public ResponseEntity<JournalEntryResponse> createJournalEntry(@RequestBody JournalEntryRequest request) {
        JournalEntryResponse response = accountingService.createJournalEntry(request);
        return ResponseEntity.ok(response);
    }
}