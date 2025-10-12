package com.example.demo.finance.mapper;

import com.example.demo.finance.dto.response.PeriodResponse;
import com.example.demo.finance.entity.FinancialPeriod;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component // Chuyển thành Spring Component
public class FinancialPeriodMapper {

    // Triển khai thủ công phương thức toResponse
    public PeriodResponse toResponse(FinancialPeriod entity) {
        if (entity == null) {
            return null;
        }
        PeriodResponse response = new PeriodResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setStartDate(entity.getStartDate());
        response.setEndDate(entity.getEndDate());
        response.setStatus(entity.getStatus());
        return response;
    }

    // Triển khai thủ công phương thức toResponseList
    public List<PeriodResponse> toResponseList(List<FinancialPeriod> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}