package com.example.demo.supplychain.service.impl;

import com.example.demo.supplychain.entity.*;
import com.example.demo.supplychain.repository.*;
import com.example.demo.supplychain.security.CurrentUserService;
import com.example.demo.supplychain.service.GoodsReceiptService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsReceiptServiceImpl implements GoodsReceiptService {

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptItemRepository goodsReceiptItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final WarehouseRepository warehouseRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceipt> getAllGoodsReceipts() {
        return goodsReceiptRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceipt getGoodsReceiptById(Integer id) {
        return goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Goods Receipt not found with id: " + id));
    }

    @Override
    @Transactional
    public GoodsReceipt createGoodsReceipt(Integer purchaseOrderId, Integer warehouseId, Integer employeeId) {
        // Validate Purchase Order
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase Order not found"));
        
        // Validate Warehouse
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));
        
        // Get current employee from security context
        Employee employee = currentUserService.getCurrentEmployee();

        // Create Goods Receipt
        GoodsReceipt goodsReceipt = GoodsReceipt.builder()
                .purchaseOrder(purchaseOrder)
                .warehouse(warehouse)
                .receiptDate(LocalDate.now())
                .createdBy(employee)
                .build();

        return goodsReceiptRepository.save(goodsReceipt);
    }

    @Override
    @Transactional
    public GoodsReceiptItem addItemToGoodsReceipt(Integer goodsReceiptId, Integer poItemId, 
                                                  Integer quantityReceived, Integer employeeId) {
        // Validate Goods Receipt
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findById(goodsReceiptId)
                .orElseThrow(() -> new EntityNotFoundException("Goods Receipt not found"));
        
        // Validate Purchase Order Item
        PurchaseOrderItem poItem = goodsReceipt.getPurchaseOrder().getItems().stream()
                .filter(item -> item.getId().equals(poItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Purchase Order Item not found"));

        // Business Logic: Validate quantity received
        if (quantityReceived > poItem.getQuantity()) {
            throw new IllegalArgumentException("Quantity received cannot exceed ordered quantity");
        }

        // Check if item already exists in goods receipt
        boolean itemExists = goodsReceipt.getItems().stream()
                .anyMatch(item -> item.getPurchaseOrderItem().getId().equals(poItemId));
        
        if (itemExists) {
            throw new IllegalArgumentException("Item already exists in this goods receipt");
        }

        // Create Goods Receipt Item
        GoodsReceiptItem receiptItem = GoodsReceiptItem.builder()
                .goodsReceipt(goodsReceipt)
                .purchaseOrderItem(poItem)
                .variant(poItem.getVariant())
                .quantityReceived(quantityReceived)
                .unitCost(poItem.getUnitPrice())
                .build();

        GoodsReceiptItem savedItem = goodsReceiptItemRepository.save(receiptItem);
        
        // Update inventory (business logic)
        updateInventory(poItem.getVariant().getId(), quantityReceived);

        return savedItem;
    }

    @Override
    @Transactional
    public void completeGoodsReceipt(Integer goodsReceiptId) {
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findById(goodsReceiptId)
                .orElseThrow(() -> new EntityNotFoundException("Goods Receipt not found"));

        // Business Logic: Check if all items are received
        PurchaseOrder purchaseOrder = goodsReceipt.getPurchaseOrder();
        boolean allItemsReceived = purchaseOrder.getItems().stream()
                .allMatch(poItem -> {
                    int totalReceived = goodsReceipt.getItems().stream()
                            .filter(grItem -> grItem.getPurchaseOrderItem().getId().equals(poItem.getId()))
                            .mapToInt(GoodsReceiptItem::getQuantityReceived)
                            .sum();
                    return totalReceived >= poItem.getQuantity();
                });

        if (!allItemsReceived) {
            throw new IllegalStateException("Cannot complete goods receipt: not all items are fully received");
        }

        // Update Purchase Order status (business logic)
        // This would require adding a status update method to PurchaseOrderService
        // For now, we'll just mark the goods receipt as completed
    }

    /**
     * Update inventory for a product variant
     */
    private void updateInventory(Integer variantId, Integer quantityReceived) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new EntityNotFoundException("Product Variant not found"));
        
        // Update current stock
        variant.setCurrentStock(variant.getCurrentStock() + quantityReceived);
        productVariantRepository.save(variant);
    }

    @Override
    @Transactional
    public void deleteGoodsReceipt(Integer id) {
        if (goodsReceiptRepository.existsById(id)) {
            goodsReceiptRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Goods Receipt not found with id: " + id);
        }
    }
}
