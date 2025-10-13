package com.example.demo.finance.controller;

import com.example.demo.finance.dto.request.AccountRequest;
import com.example.demo.finance.dto.request.UpdateAccountRequest;
import com.example.demo.finance.dto.response.AccountResponse;
import com.example.demo.finance.service.ChartOfAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/accounts")
@RequiredArgsConstructor
public class ChartOfAccountsController {

    private final ChartOfAccountsService service;

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_FINANCE')")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        AccountResponse response = service.createAccount(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_FINANCE')")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> responses = service.getAllAccounts();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_FINANCE')") // Chỉ cần một annotation là đủ
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer id) {
        service.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_FINANCE')") // Bảo vệ API sửa
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Integer id, @RequestBody UpdateAccountRequest request) {
        AccountResponse response = service.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }
}