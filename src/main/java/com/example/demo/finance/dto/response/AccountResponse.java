package com.example.demo.finance.dto.response;

import lombok.Data;

@Data
public class AccountResponse {
    private Integer id;
    private String accountCode;
    private String accountName;
    private String accountType;
    private Boolean isActive;
}