package com.example.demo.finance.service.impl;

import com.example.demo.finance.dto.request.AccountRequest;
import com.example.demo.finance.dto.request.UpdateAccountRequest;
import com.example.demo.finance.dto.response.AccountResponse;
import com.example.demo.finance.entity.ChartOfAccounts;
import com.example.demo.finance.mapper.ChartOfAccountsMapper; // THÊM IMPORT
import com.example.demo.finance.repository.ChartOfAccountsRepository;
import com.example.demo.finance.service.ChartOfAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
// import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChartOfAccountsServiceImpl implements ChartOfAccountsService {

    private final ChartOfAccountsRepository repository;
    private final ChartOfAccountsMapper mapper; // INJECT MAPPER

    @Override
    public AccountResponse createAccount(AccountRequest request) {
        ChartOfAccounts newAccount = ChartOfAccounts.builder()
                .accountCode(request.getAccountCode())
                .accountName(request.getAccountName())
                .accountType(request.getAccountType())
                .description(request.getDescription())
                .isActive(true)
                .build();

        ChartOfAccounts savedAccount = repository.save(newAccount);

        // DÙNG MAPPER ĐỂ CHUYỂN ĐỔI
        return mapper.toResponse(savedAccount);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        List<ChartOfAccounts> accounts = repository.findAll();

        // DÙNG MAPPER ĐỂ CHUYỂN ĐỔI CẢ LIST
        return mapper.toResponseList(accounts);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        ChartOfAccounts account = repository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        // Xóa mềm: chỉ đổi trạng thái, không xóa khỏi DB
        account.setIsActive(false);

        repository.save(account);
    }

    @Override
    public AccountResponse updateAccount(Integer accountId, UpdateAccountRequest request) {
        // 1. Tìm tài khoản hiện có trong DB
        ChartOfAccounts existingAccount = repository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        // 2. Cập nhật các trường thông tin từ request DTO
        existingAccount.setAccountName(request.getAccountName());
        existingAccount.setAccountType(request.getAccountType());
        existingAccount.setDescription(request.getDescription());

        // 3. Lưu lại vào DB
        ChartOfAccounts updatedAccount = repository.save(existingAccount);

        // 4. Dùng mapper để trả về kết quả
        return mapper.toResponse(updatedAccount);
    }
}