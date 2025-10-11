package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
    private String employeeCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "fullname", nullable = false, length = 100)
    private String fullname;

    @Column(length = 100)
    private String position;

    @Column(length = 100)
    private String department;

    @Column(name = "hired_date", nullable = false)
    private LocalDate hiredDate;

    @Default
    @Column(name = "is_active")
    private boolean isActive = true;
}