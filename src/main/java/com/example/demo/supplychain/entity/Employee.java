//package com.example.demo.supplychain.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "supplychain_employees")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//public class Employee {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(name = "fullname", nullable = false, length = 255)
//    private String fullname;
//
//    @Column(name = "email", nullable = false, length = 100)
//    private String email;
//
//    @Column(name = "phone", length = 20)
//    private String phone;
//
//    @Column(name = "position", length = 100)
//    private String position;
//
//    @Column(name = "department", length = 100)
//    private String department;
//
//    @Column(name = "is_active", nullable = false)
//    @Builder.Default
//    private Boolean isActive = true;
//}
