package com.example.demo.finance.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PeriodRequest {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}