package com.example.demo.supplychain.service;

import com.example.demo.supplychain.entity.PurchaseOrderItem;
import com.example.demo.supplychain.repository.PurchaseOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderItemService {

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    public List<PurchaseOrderItem> findAllPurchaseOrderItems() {
        return purchaseOrderItemRepository.findAll();
    }

    public Optional<PurchaseOrderItem> findPurchaseOrderItemById(Integer id) {
        return purchaseOrderItemRepository.findById(id);
    }

    public PurchaseOrderItem savePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        return purchaseOrderItemRepository.save(purchaseOrderItem);
    }

    public void deletePurchaseOrderItem(Integer id) {
        purchaseOrderItemRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return purchaseOrderItemRepository.existsById(id);
    }
}
