package com.example.demo.finance.repository;

import com.example.demo.finance.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    // Tìm tất cả bút toán liên quan đến "ORDER" trong một khoảng thời gian
    List<JournalEntry> findByReferenceTypeAndTransactionDateBetween(
            String referenceType,
            OffsetDateTime startDate,
            OffsetDateTime endDate);
}