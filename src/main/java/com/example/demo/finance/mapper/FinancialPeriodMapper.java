package com.example.demo.finance.mapper;

import com.example.demo.finance.dto.response.PeriodResponse;
import com.example.demo.finance.entity.FinancialPeriod;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FinancialPeriodMapper {

    PeriodResponse toResponse(FinancialPeriod entity);

    List<PeriodResponse> toResponseList(List<FinancialPeriod> entities);
}