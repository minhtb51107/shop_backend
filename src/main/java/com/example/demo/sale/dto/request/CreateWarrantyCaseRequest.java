package com.example.demo.sale.dto.request;

import lombok.Data;

@Data
public class CreateWarrantyCaseRequest {
    private Long orderItemId;
    private String serialNumber;
    private Long customerId;
    private String description;
    private Integer createdByEmployeeId;
}