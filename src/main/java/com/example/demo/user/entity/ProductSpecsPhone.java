package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_specs_phone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecsPhone {
    @Id
    @Column(name = "product_id")
    private Integer productId;

    private String screenSize;
    private String ram;
    private String storage;
    private String battery;
    private String os;
}
