package com.example.demo.product_inventory.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "inventory_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long variantId;
    private Integer warehouseId;
    private Integer quantityChange;
    private String type;
    private String referenceId;
    private Integer createdByEmployeeId;
    private ZonedDateTime createdAt = ZonedDateTime.now();
}

