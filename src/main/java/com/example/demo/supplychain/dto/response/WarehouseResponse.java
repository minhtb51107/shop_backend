package com.example.demo.supplychain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseResponse {
    private Integer id;
    private String name;
    private String address;
}