package com.example.demo.product_inventory.repository;

import com.example.demo.product_inventory.entity.Inventory;
import com.example.demo.product_inventory.entity.InventoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {

    // ✅ Tìm tồn kho theo Variant ID (đúng vì Inventory có thuộc tính variant)
    List<Inventory> findByVariant_Id(Long variantId);

    // ✅ Tìm tồn kho theo Product ID (đi qua quan hệ variant → product)
    List<Inventory> findByVariant_Product_Id(Long productId);
}
