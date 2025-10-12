package com.example.demo.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}
