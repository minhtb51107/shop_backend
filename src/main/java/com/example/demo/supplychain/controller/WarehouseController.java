package com.example.demo.supplychain.controller;

import com.example.demo.supplychain.entity.Warehouse;
import com.example.demo.supplychain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;

    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Integer id) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(id);
        return warehouse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return ResponseEntity.ok(savedWarehouse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable Integer id, @RequestBody Warehouse warehouse) {
        if (!warehouseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        warehouse.setId(id);
        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        return ResponseEntity.ok(updatedWarehouse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Integer id) {
        if (!warehouseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        warehouseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/web")
    public ResponseEntity<String> getWarehouseWeb() {
        return ResponseEntity.ok("Warehouse Web Interface");
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Warehouse>> searchWarehousesByName(@RequestParam String name) {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        // Simple name filtering - in real app, use proper query
        List<Warehouse> filtered = warehouses.stream()
                .filter(w -> w.getName() != null && w.getName().contains(name))
                .toList();
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/search/address")
    public ResponseEntity<List<Warehouse>> searchWarehousesByAddress(@RequestParam String address) {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        // Simple address filtering - in real app, use proper query
        List<Warehouse> filtered = warehouses.stream()
                .filter(w -> w.getAddress() != null && w.getAddress().contains(address))
                .toList();
        return ResponseEntity.ok(filtered);
    }
}
