package com.example.demo.finance.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PeriodResponse {
    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}