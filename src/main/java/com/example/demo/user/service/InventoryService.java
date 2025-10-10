package com.example.demo.user.service;

import com.example.demo.user.entity.Inventory;
import com.example.demo.user.entity.InventoryId;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> findAll();
    Optional<Inventory> findById(InventoryId id);
    Inventory save(Inventory inventory);
    void deleteById(InventoryId id);
}
