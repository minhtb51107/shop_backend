package com.example.demo.finance.repository;

import com.example.demo.finance.entity.JournalEntryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalEntryItemRepository extends JpaRepository<JournalEntryItem, Long> {
}