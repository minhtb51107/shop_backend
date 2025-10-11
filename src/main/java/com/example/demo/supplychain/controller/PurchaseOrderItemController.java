package com.example.demo.supplychain.controller;

import com.example.demo.supplychain.entity.PurchaseOrderItem;
import com.example.demo.supplychain.repository.PurchaseOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/purchase-order-items")
@RequiredArgsConstructor
public class PurchaseOrderItemController {

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    @GetMapping
    public ResponseEntity<List<PurchaseOrderItem>> getAllPurchaseOrderItems() {
        List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemRepository.findAll();
        return ResponseEntity.ok(purchaseOrderItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderItem> getPurchaseOrderItemById(@PathVariable Integer id) {
        Optional<PurchaseOrderItem> purchaseOrderItem = purchaseOrderItemRepository.findById(id);
        return purchaseOrderItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderItem> createPurchaseOrderItem(@RequestBody PurchaseOrderItem purchaseOrderItem) {
        PurchaseOrderItem savedPurchaseOrderItem = purchaseOrderItemRepository.save(purchaseOrderItem);
        return ResponseEntity.ok(savedPurchaseOrderItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderItem> updatePurchaseOrderItem(@PathVariable Integer id, @RequestBody PurchaseOrderItem purchaseOrderItem) {
        if (!purchaseOrderItemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        purchaseOrderItem.setId(id);
        PurchaseOrderItem updatedPurchaseOrderItem = purchaseOrderItemRepository.save(purchaseOrderItem);
        return ResponseEntity.ok(updatedPurchaseOrderItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrderItem(@PathVariable Integer id) {
        if (!purchaseOrderItemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        purchaseOrderItemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
