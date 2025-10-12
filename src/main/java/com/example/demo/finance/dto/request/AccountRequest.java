package com.example.demo.finance.dto.request;

import lombok.Data;

@Data
public class AccountRequest {
    private String accountCode;
    private String accountName;
    private String accountType;
    private String description;
}