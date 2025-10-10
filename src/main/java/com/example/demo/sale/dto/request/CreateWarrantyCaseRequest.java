package com.example.demo.sale.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWarrantyCaseRequest {
    private Long orderItemId;
    private String serialNumber;
    private Long customerId;
    private String description;
    private Long createdByEmployeeId;
}