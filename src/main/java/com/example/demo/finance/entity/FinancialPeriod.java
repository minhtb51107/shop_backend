package com.example.demo.finance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "financial_periods")
public class FinancialPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "start_date", nullable = false, unique = true)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false, unique = true)
    private LocalDate endDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status;
}