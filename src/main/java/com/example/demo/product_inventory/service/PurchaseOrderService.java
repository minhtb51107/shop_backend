// File: service/PurchaseOrderService.java
package com.example.demo.product_inventory.service;

import com.example.demo.product_inventory.dto.request.PurchaseOrderRequest;
import com.example.demo.product_inventory.dto.response.PurchaseOrderResponse;
import java.util.List;
import java.util.Optional;

public interface PurchaseOrderService {
    PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request);
    Optional<PurchaseOrderResponse> findById(Long id);
    List<PurchaseOrderResponse> findAll();
}