// src/main/java/com/example/demo/product/service/PurchaseOrderItemService.java
package com.example.demo.product.service;

import com.example.demo.product.dto.request.PurchaseOrderItemRequest;
import com.example.demo.product.dto.response.PurchaseOrderItemResponse;

public interface PurchaseOrderItemService {
    PurchaseOrderItemResponse addItemToPurchaseOrder(Integer orderId, PurchaseOrderItemRequest request);
    
    // *** THÊM CÁC PHƯƠNG THỨC MỚI ***
    PurchaseOrderItemResponse updateItem(Long itemId, PurchaseOrderItemRequest request);
    
    void deleteItem(Long itemId);
}