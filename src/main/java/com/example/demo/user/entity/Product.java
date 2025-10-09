package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "sku_prefix", unique = true, length = 50)
    private String skuPrefix;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Default
    @Column(name = "is_active")
    private boolean isActive = true;

    @Default
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}