package com.example.demo.product.controller;

import com.example.demo.product.dto.request.CreateInventoryRequest;
import com.example.demo.product.dto.request.UpdateStockRequest;
import com.example.demo.product.dto.response.InventoryResponse;
import com.example.demo.product.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        List<InventoryResponse> inventoryList = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventoryList);
    }

    @GetMapping("/{variantId}/{warehouseId}")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable Long variantId, @PathVariable Integer warehouseId) {
        return inventoryService.getInventoryById(variantId, warehouseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@RequestBody CreateInventoryRequest request) {
        InventoryResponse newInventory = inventoryService.createInventory(request);
        return new ResponseEntity<>(newInventory, HttpStatus.CREATED);
    }

    @PutMapping("/{variantId}/{warehouseId}")
    public ResponseEntity<InventoryResponse> updateInventoryStock(
            @PathVariable Long variantId,
            @PathVariable Integer warehouseId,
            @RequestBody UpdateStockRequest request) {
        return inventoryService.updateInventoryStock(variantId, warehouseId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{variantId}/{warehouseId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long variantId, @PathVariable Integer warehouseId) {
        boolean isDeleted = inventoryService.deleteInventory(variantId, warehouseId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}