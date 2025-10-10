package com.example.demo.sale.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyCaseResponse {
    private Long id;
    private Long orderItemId;
    private String serialNumber;
    private Long customerId;
    private String status;
    private String description;
    private Long createdByEmployeeId;
}