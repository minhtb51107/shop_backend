package com.example.demo.finance.dto.request;

import lombok.Data;

@Data
public class UpdateAccountRequest {
    // Thường thì mã tài khoản (accountCode) sẽ không được phép sửa
    // Chúng ta chỉ cho phép sửa tên, mô tả và loại tài khoản
    private String accountName;
    private String accountType;
    private String description;
}