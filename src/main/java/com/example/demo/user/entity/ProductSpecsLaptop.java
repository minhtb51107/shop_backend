package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_specs_laptop")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecsLaptop {
    @Id
    @Column(name = "product_id")
    private Integer productId;

    private String cpu;
    private String ram;
    private String storage;
    private String gpu;
    private String screenSize;
}
