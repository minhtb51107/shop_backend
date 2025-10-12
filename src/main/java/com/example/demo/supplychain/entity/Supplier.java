package com.example.demo.supplychain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "suppliers")
@Data
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    // Ánh xạ trường camelCase "contactPerson" với cột snake_case "contact_person"
    @Column(name = "contact_person")
    private String contactPerson;

    // Sửa lại tên trường cho đúng với cột "email"
    private String email;
}