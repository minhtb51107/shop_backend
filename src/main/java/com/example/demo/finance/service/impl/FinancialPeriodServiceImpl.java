package com.example.demo.finance.service.impl;

import com.example.demo.finance.dto.request.PeriodRequest;
import com.example.demo.finance.dto.response.PeriodResponse;
import com.example.demo.finance.entity.FinancialPeriod;
import com.example.demo.finance.repository.FinancialPeriodRepository;
import com.example.demo.finance.service.FinancialPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.finance.mapper.FinancialPeriodMapper;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FinancialPeriodServiceImpl implements FinancialPeriodService {

    private final FinancialPeriodRepository repository;
    private final FinancialPeriodMapper mapper; // Inject mapper vào

    @Override
    public PeriodResponse createPeriod(PeriodRequest request) {
        FinancialPeriod newPeriod = FinancialPeriod.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status("OPEN")
                .build();

        FinancialPeriod savedPeriod = repository.save(newPeriod);
        return mapper.toResponse(savedPeriod); // Dùng mapper
    }

    @Override
    public List<PeriodResponse> getAllPeriods() {
        return mapper.toResponseList(repository.findAll()); // Dùng mapper
    }

    @Override
    public PeriodResponse closePeriod(Integer periodId) {
        FinancialPeriod period = repository.findById(periodId)
                .orElseThrow(() -> new RuntimeException("Period not found with id: " + periodId));

        period.setStatus("CLOSED");

        FinancialPeriod updatedPeriod = repository.save(period);
        return mapper.toResponse(updatedPeriod); // Dùng mapper
    }
}