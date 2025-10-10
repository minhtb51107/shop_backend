package com.example.demo.user.repository;

import com.example.demo.user.entity.Inventory;
import com.example.demo.user.entity.InventoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {
    // Nếu muốn tìm theo product_id
    List<Inventory> findByProduct_ProductId(Long productId);

    // Nếu muốn tìm theo variant_id
    List<Inventory> findByVariant_VariantId(Long variantId);
}
