package com.example.demo.product_inventory.service.impl;

import com.example.demo.product_inventory.dto.request.CreateInventoryRequest;
import com.example.demo.product_inventory.dto.request.UpdateStockRequest;
import com.example.demo.product_inventory.dto.response.InventoryResponse;
import com.example.demo.product_inventory.entity.Inventory;
import com.example.demo.product_inventory.entity.InventoryId;
import com.example.demo.product_inventory.entity.ProductVariant;
import com.example.demo.product_inventory.entity.Warehouse;
import com.example.demo.product_inventory.mapper.InventoryMapper;
import com.example.demo.product_inventory.repository.InventoryRepository;
import com.example.demo.product_inventory.repository.VariantRepository;
import com.example.demo.product_inventory.repository.WarehouseRepository;
import com.example.demo.product_inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final VariantRepository variantRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                VariantRepository variantRepository,
                                WarehouseRepository warehouseRepository,
                                InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.variantRepository = variantRepository;
        this.warehouseRepository = warehouseRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryResponse> getInventoryById(Long variantId, Integer warehouseId) {
        InventoryId id = new InventoryId(variantId, warehouseId);
        return inventoryRepository.findById(id).map(inventoryMapper::toResponse);
    }

    @Override
    @Transactional
    public InventoryResponse createInventory(CreateInventoryRequest request) {
        InventoryId id = new InventoryId(request.getVariantId(), request.getWarehouseId());

        if (inventoryRepository.existsById(id)) {
            throw new IllegalStateException("Inventory record already exists for this variant and warehouse.");
        }

        ProductVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new IllegalArgumentException("Variant not found with id: " + request.getVariantId()));
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with id: " + request.getWarehouseId()));

        Inventory newInventory = new Inventory();
        newInventory.setId(id);
        newInventory.setVariant(variant);
        newInventory.setWarehouse(warehouse);
        newInventory.setStockQuantity(request.getInitialStock());

        Inventory savedInventory = inventoryRepository.save(newInventory);
        return inventoryMapper.toResponse(savedInventory);
    }

    @Override
    @Transactional
    public Optional<InventoryResponse> updateInventoryStock(Long variantId, Integer warehouseId, UpdateStockRequest request) {
        InventoryId id = new InventoryId(variantId, warehouseId);
        return inventoryRepository.findById(id)
                .map(existingInventory -> {
                    existingInventory.setStockQuantity(request.getStockQuantity());
                    Inventory updatedInventory = inventoryRepository.save(existingInventory);
                    return inventoryMapper.toResponse(updatedInventory);
                });
    }

    @Override
    @Transactional
    public boolean deleteInventory(Long variantId, Integer warehouseId) {
        InventoryId id = new InventoryId(variantId, warehouseId);
        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}