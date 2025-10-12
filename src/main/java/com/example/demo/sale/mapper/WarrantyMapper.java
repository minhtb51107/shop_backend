package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreateWarrantyCaseRequest;
import com.example.demo.sale.dto.response.WarrantyCaseResponse;
import com.example.demo.sale.entity.WarrantyCase;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarrantyMapper {
    public WarrantyCase toEntity(CreateWarrantyCaseRequest request) {
        if (request == null) {
            return null;
        }
        WarrantyCase warrantyCase = new WarrantyCase();
        warrantyCase.setSerialNumber(request.getSerialNumber());
        warrantyCase.setDescription(request.getDescription());
        return warrantyCase;
    }

    public WarrantyCaseResponse toDto(WarrantyCase warrantyCase) {
        if (warrantyCase == null) {
            return null;
        }
        WarrantyCaseResponse dto = new WarrantyCaseResponse();
        dto.setId(warrantyCase.getId());
        if (warrantyCase.getOrderItem() != null) {
            dto.setOrderItemId(warrantyCase.getOrderItem().getId());
        }
        dto.setSerialNumber(warrantyCase.getSerialNumber());
        if (warrantyCase.getCustomer() != null) {
            dto.setCustomerId(warrantyCase.getCustomer().getId().longValue());
        }
        dto.setStatus(warrantyCase.getStatus());
        dto.setDescription(warrantyCase.getDescription());
        if (warrantyCase.getCreatedBy() != null) {
            dto.setCreatedByEmployeeId(warrantyCase.getCreatedBy().getId());
        }
        return dto;
    }

    public List<WarrantyCaseResponse> toDtoList(List<WarrantyCase> cases) {
        return cases.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}