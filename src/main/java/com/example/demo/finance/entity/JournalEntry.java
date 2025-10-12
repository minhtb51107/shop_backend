package com.example.demo.finance.entity;

// Lưu ý: Dòng import Employee sẽ báo lỗi vì module đó chưa có.
// Bạn có thể tạo một class Employee giả tạm thời để code không báo lỗi.
// import com.example.demo.user.entity.Employee;

import com.example.demo.user.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "journal_entries")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private FinancialPeriod period;

    @Column(name = "transaction_date", nullable = false)
    private OffsetDateTime transactionDate;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "created_by_employee_id")
     private Employee createdBy;

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalEntryItem> items;
}