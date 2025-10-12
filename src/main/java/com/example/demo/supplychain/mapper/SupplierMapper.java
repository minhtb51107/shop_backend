package com.example.demo.supplychain.mapper;

import com.example.demo.supplychain.dto.response.SupplierResponse;
import com.example.demo.supplychain.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
    public SupplierResponse toSupplierResponse(Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactPerson(supplier.getContactPerson())
                .email(supplier.getEmail())
                .build();
    }
}