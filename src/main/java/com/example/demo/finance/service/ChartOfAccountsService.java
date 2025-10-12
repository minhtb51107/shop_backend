package com.example.demo.finance.service;

import com.example.demo.finance.dto.request.AccountRequest;
import com.example.demo.finance.dto.request.UpdateAccountRequest;
import com.example.demo.finance.dto.response.AccountResponse;
import java.util.List;

public interface ChartOfAccountsService {
    AccountResponse createAccount(AccountRequest request);
    List<AccountResponse> getAllAccounts();
    void deleteAccount(Integer accountId);
    AccountResponse updateAccount(Integer accountId, UpdateAccountRequest request);
}