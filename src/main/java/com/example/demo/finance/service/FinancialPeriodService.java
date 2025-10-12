package com.example.demo.finance.service;

import com.example.demo.finance.dto.request.PeriodRequest;
import com.example.demo.finance.dto.response.PeriodResponse;
import java.util.List;

public interface FinancialPeriodService {
    PeriodResponse createPeriod(PeriodRequest request);
    List<PeriodResponse> getAllPeriods();
    PeriodResponse closePeriod(Integer periodId);
}