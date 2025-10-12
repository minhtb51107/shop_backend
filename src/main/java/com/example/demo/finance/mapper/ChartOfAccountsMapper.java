package com.example.demo.finance.mapper;

import com.example.demo.finance.dto.response.AccountResponse;
import com.example.demo.finance.entity.ChartOfAccounts;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component // Chuyển thành Spring Component
public class ChartOfAccountsMapper {

    // Triển khai thủ công phương thức toResponse
    public AccountResponse toResponse(ChartOfAccounts entity) {
        if (entity == null) {
            return null;
        }
        AccountResponse response = new AccountResponse();
        response.setId(entity.getId());
        response.setAccountCode(entity.getAccountCode());
        response.setAccountName(entity.getAccountName());
        response.setAccountType(entity.getAccountType());
        response.setIsActive(entity.getIsActive());
        return response;
    }

    // Triển khai thủ công phương thức toResponseList
    public List<AccountResponse> toResponseList(List<ChartOfAccounts> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}