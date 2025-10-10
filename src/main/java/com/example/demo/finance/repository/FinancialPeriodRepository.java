package com.example.demo.finance.repository;

import com.example.demo.finance.entity.FinancialPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialPeriodRepository extends JpaRepository<FinancialPeriod, Integer> {
}