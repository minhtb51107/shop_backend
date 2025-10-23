package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.CreateInventoryRequest;
import com.example.demo.product.dto.request.UpdateStockRequest;
import com.example.demo.product.dto.response.InventoryResponse;
import com.example.demo.product.entity.Inventory;
import com.example.demo.product.entity.InventoryId;
import com.example.demo.product.entity.ProductVariant;
import com.example.demo.product.mapper.InventoryMapper;
import com.example.demo.product.repository.InventoryRepository;
import com.example.demo.product.repository.VariantRepository;
import com.example.demo.product.service.InventoryService;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import com.example.demo.supplychain.entity.Warehouse;
import com.example.demo.supplychain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final VariantRepository variantRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public InventoryResponse createInventory(CreateInventoryRequest request) {
        Integer warehouseId = request.getWarehouseId(); // Request DTO dùng Integer
        if (warehouseId == null) {
            throw new BadRequestException("Warehouse ID is required.");
        }
        Long variantId = request.getVariantId(); // Request DTO dùng Long
        if (variantId == null) {
            throw new BadRequestException("Variant ID is required.");
        }

        InventoryId inventoryId = new InventoryId(variantId, warehouseId);
        if (inventoryRepository.existsById(inventoryId)) {
            log.warn("Inventory already exists for Variant ID: {} and Warehouse ID: {}", variantId, warehouseId);
            throw new BadRequestException("Inventory already exists for this variant and warehouse.");
        }

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));
        // Tìm Warehouse bằng Long ID (vì repo dùng Long)
        Warehouse warehouse = warehouseRepository.findById((int) warehouseId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + warehouseId));

        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);
        inventory.setVariant(variant);
        inventory.setWarehouse(warehouse);
        inventory.setStockQuantity(request.getInitialStock() != null ? request.getInitialStock() : 0); // Dùng initialStock

        Inventory savedInventory = inventoryRepository.save(inventory);
        log.info("Created new inventory for Variant ID: {} at Warehouse ID: {} with initial quantity: {}",
                 variantId, warehouseId, inventory.getStockQuantity());

        return inventoryMapper.toResponse(savedInventory); // Dùng mapper method toResponse
    }

    // --- Chữ ký phương thức phải khớp interface (Long warehouseId) ---
    @Override
    public InventoryResponse getInventory(Long variantId, Long warehouseId) {
        if (warehouseId == null) {
            throw new BadRequestException("Warehouse ID cannot be null.");
        }
        if (variantId == null) {
             throw new BadRequestException("Variant ID cannot be null.");
        }
        // Chuyển Long warehouseId thành Integer để tạo InventoryId
        Integer warehouseIdInt = warehouseId.intValue();
        InventoryId inventoryId = new InventoryId(variantId, warehouseIdInt);

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for variant " + variantId + " at warehouse " + warehouseIdInt));
        return inventoryMapper.toResponse(inventory); // Dùng mapper method toResponse
    }

    @Override
    public List<InventoryResponse> getAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toResponse) // Dùng mapper method toResponse
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getInventoriesByVariant(Long variantId) {
        if (variantId == null) {
             throw new BadRequestException("Variant ID cannot be null.");
        }
        log.debug("Fetching all inventories to filter by Variant ID: {}", variantId);
        return inventoryRepository.findAll().stream()
                // Dùng getVariant()
                .filter(inventory -> inventory.getVariant() != null && Objects.equals(inventory.getVariant().getId(), variantId))
                .map(inventoryMapper::toResponse) // Dùng mapper method toResponse
                .collect(Collectors.toList());
    }

    // --- Chữ ký phương thức phải khớp interface (Long warehouseId) ---
    @Override
    public List<InventoryResponse> getInventoriesByWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            throw new BadRequestException("Warehouse ID cannot be null.");
        }
        // Chuyển Long warehouseId thành Integer để so sánh
        Integer warehouseIdInt = warehouseId.intValue();
        log.debug("Fetching all inventories to filter by Warehouse ID: {}", warehouseIdInt);

        return inventoryRepository.findAll().stream()
                // So sánh Integer warehouseId lấy từ InventoryId
                .filter(inventory -> inventory.getId() != null && Objects.equals(inventory.getId().getWarehouseId(), warehouseIdInt))
                .map(inventoryMapper::toResponse) // Dùng mapper method toResponse
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryResponse updateStock(UpdateStockRequest request) {
        log.info("Updating stock for Variant ID: {} at Warehouse ID: {} with change: {} of type: {}",
                 request.getVariantId(), request.getWarehouseId(), request.getQuantityChange(), request.getTransactionType());

        // UpdateStockRequest DTO dùng Integer cho warehouseId, Long cho variantId
        Integer warehouseId = request.getWarehouseId();
        Long variantId = request.getVariantId();
        if (warehouseId == null || variantId == null) {
            throw new BadRequestException("Variant ID and Warehouse ID are required in UpdateStockRequest.");
        }

        InventoryId inventoryId = new InventoryId(variantId, warehouseId);
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> {
                     log.error("Inventory not found for Variant ID: {} at Warehouse ID: {}", variantId, warehouseId);
                     return new ResourceNotFoundException("Inventory not found for variant " + variantId + " at warehouse " + warehouseId);
                });

        int quantityChange = request.getQuantityChange() != null ? request.getQuantityChange() : 0;
        String transactionType = request.getTransactionType() != null ? request.getTransactionType().toUpperCase() : "UNKNOWN";

        if (("SALE".equals(transactionType) || "OUT".equals(transactionType) || "IN".equals(transactionType) || "RETURN".equals(transactionType)) && quantityChange <= 0) {
             log.error("Invalid quantity change ({}) for transaction type: {}", quantityChange, transactionType);
             throw new BadRequestException("Quantity change must be positive for transaction type: " + transactionType);
        }

        // Dùng getStockQuantity()
        int currentQuantity = inventory.getStockQuantity() != null ? inventory.getStockQuantity() : 0;
        int newQuantity;

        switch (transactionType) {
            case "SALE":
            case "OUT":
                if (currentQuantity < quantityChange) {
                    log.error("Insufficient stock for Variant ID: {}. Required: {}, Available: {}", variantId, quantityChange, currentQuantity);
                    throw new BadRequestException("Insufficient stock for variant ID " + variantId + ". Required: " + quantityChange + ", Available: " + currentQuantity);
                }
                newQuantity = currentQuantity - quantityChange;
                log.info("Stock reduced from {} to {} for Variant ID: {}", currentQuantity, newQuantity, variantId);
                break;
            case "IN":
            case "RETURN":
                newQuantity = currentQuantity + quantityChange;
                log.info("Stock increased from {} to {} for Variant ID: {}", currentQuantity, newQuantity, variantId);
                break;
            case "ADJUSTMENT":
                 newQuantity = currentQuantity + quantityChange;
                 if (newQuantity < 0) {
                      log.warn("Adjustment resulted in negative stock for Variant ID: {}. Setting stock to 0.", variantId);
                      newQuantity = 0;
                 }
                 log.info("Stock adjusted from {} to {} for Variant ID: {}", currentQuantity, newQuantity, variantId);
                 break;
            default:
                 log.error("Invalid transaction type received: {}", transactionType);
                throw new BadRequestException("Invalid or unsupported transaction type: " + transactionType);
        }

        // Dùng setStockQuantity()
        inventory.setStockQuantity(newQuantity);
        Inventory savedInventory = inventoryRepository.save(inventory);
        log.info("Inventory saved successfully for Variant ID: {} with new quantity: {}", variantId, newQuantity);

        return inventoryMapper.toResponse(savedInventory); // Dùng mapper method toResponse
    }

     // --- Chữ ký phương thức phải khớp interface (Long warehouseId) ---
     @Override
     public boolean checkStockAvailability(Long variantId, Long warehouseId, int quantity) {
          if (warehouseId == null) {
              log.error("Stock availability check failed: Warehouse ID is null for Variant ID: {}", variantId);
              return false; // Hoặc throw BadRequestException
          }
          if (variantId == null) {
               log.error("Stock availability check failed: Variant ID is null.");
               return false; // Hoặc throw BadRequestException
          }
          // Chuyển Long warehouseId thành Integer để tạo InventoryId
          Integer warehouseIdInt = warehouseId.intValue();

          if (quantity <= 0) {
              log.warn("Stock check requested with non-positive quantity ({}) for Variant ID: {}", quantity, variantId);
              return true;
          }

          InventoryId inventoryId = new InventoryId(variantId, warehouseIdInt);
          boolean isAvailable = inventoryRepository.findById(inventoryId)
                  // Dùng getStockQuantity()
                  .map(inventory -> inventory.getStockQuantity() != null && inventory.getStockQuantity() >= quantity)
                  .orElse(false);
          log.debug("Stock check for Variant ID: {}, Warehouse ID: {}, Requested: {} -> Available: {}",
                    variantId, warehouseIdInt, quantity, isAvailable);
          return isAvailable;
     }
}