package com.example.demo.product_inventory.service;

import com.example.demo.product_inventory.dto.request.CreateInventoryRequest;
import com.example.demo.product_inventory.dto.request.UpdateStockRequest;
import com.example.demo.product_inventory.dto.response.InventoryResponse;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<InventoryResponse> getAllInventory();
    Optional<InventoryResponse> getInventoryById(Long variantId, Integer warehouseId);
    InventoryResponse createInventory(CreateInventoryRequest request);
    Optional<InventoryResponse> updateInventoryStock(Long variantId, Integer warehouseId, UpdateStockRequest request);
    boolean deleteInventory(Long variantId, Integer warehouseId);
}