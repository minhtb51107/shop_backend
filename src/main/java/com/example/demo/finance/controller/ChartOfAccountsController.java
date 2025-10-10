package com.example.demo.finance.controller;

import com.example.demo.finance.dto.request.AccountRequest;
import com.example.demo.finance.dto.response.AccountResponse;
import com.example.demo.finance.service.ChartOfAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finance/accounts")
@RequiredArgsConstructor
public class ChartOfAccountsController {

    private final ChartOfAccountsService service;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        AccountResponse response = service.createAccount(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> responses = service.getAllAccounts();
        return ResponseEntity.ok(responses);
    }
}