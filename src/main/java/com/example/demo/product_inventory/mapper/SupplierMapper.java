// File: mapper/SupplierMapper.java
package com.example.demo.product_inventory.mapper;

import com.example.demo.product_inventory.dto.request.SupplierRequest;
import com.example.demo.product_inventory.dto.response.SupplierResponse;
import com.example.demo.product_inventory.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierResponse toResponse(Supplier entity) {
        if (entity == null) return null;
        SupplierResponse dto = new SupplierResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setContactPerson(entity.getContactPerson());
        dto.setEmail(entity.getEmail()); // Bây giờ sẽ hoạt động!
        return dto;
    }

    public Supplier toEntity(SupplierRequest dto) {
        if (dto == null) return null;
        Supplier entity = new Supplier();
        entity.setName(dto.getName());
        entity.setContactPerson(dto.getContactPerson());
        entity.setEmail(dto.getEmail()); // Bây giờ sẽ hoạt động!
        return entity;
    }
}