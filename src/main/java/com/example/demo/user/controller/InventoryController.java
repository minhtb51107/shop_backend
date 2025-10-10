package com.example.demo.user.controller;

import com.example.demo.user.entity.Inventory;
import com.example.demo.user.entity.InventoryId;
import com.example.demo.user.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @GetMapping("/{variantId}/{warehouseId}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long variantId, @PathVariable Integer warehouseId) {
        InventoryId id = new InventoryId(variantId, warehouseId);
        return inventoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Inventory createInventory(@RequestBody Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @PutMapping("/{variantId}/{warehouseId}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long variantId, @PathVariable Integer warehouseId, @RequestBody Inventory details) {
        InventoryId id = new InventoryId(variantId, warehouseId);
        return inventoryRepository.findById(id)
                .map(inventory -> {
                    inventory.setStockQuantity(details.getStockQuantity());
                    return ResponseEntity.ok(inventoryRepository.save(inventory));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{variantId}/{warehouseId}")
    public ResponseEntity<Object> deleteInventory(@PathVariable Long variantId, @PathVariable Integer warehouseId) {
        InventoryId id = new InventoryId(variantId, warehouseId);
        return inventoryRepository.findById(id)
                .map(inventory -> {
                    inventoryRepository.delete(inventory);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
