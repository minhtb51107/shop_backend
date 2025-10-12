package com.example.demo.finance.repository;

import com.example.demo.finance.entity.ChartOfAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, Integer> {
}