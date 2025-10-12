package com.example.demo.supplychain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierResponse {
    private Integer id;
    private String name;
    private String contactPerson;
    private String email;
}