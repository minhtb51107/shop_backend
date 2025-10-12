package com.example.demo.supplychain.mapper;

import com.example.demo.supplychain.dto.response.WarehouseResponse;
import com.example.demo.supplychain.entity.Warehouse;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper {
    public WarehouseResponse toWarehouseResponse(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }
        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .address(warehouse.getAddress())
                .build();
    }
}