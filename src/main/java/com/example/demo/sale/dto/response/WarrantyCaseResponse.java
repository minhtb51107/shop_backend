package com.example.demo.sale.dto.response;

import lombok.Data;

@Data
public class WarrantyCaseResponse {
    private Long id;
    private Long orderItemId;
    private String serialNumber;
    private Long customerId;
    private String status;
    private String description;
    private Integer createdByEmployeeId;
}