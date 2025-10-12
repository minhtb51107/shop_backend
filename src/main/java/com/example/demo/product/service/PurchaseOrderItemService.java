package com.example.demo.product.service;


import com.example.demo.product.dto.request.PurchaseOrderItemRequest;
import com.example.demo.product.dto.response.PurchaseOrderItemResponse;

public interface PurchaseOrderItemService {
    PurchaseOrderItemResponse addItemToPurchaseOrder(Long orderId, PurchaseOrderItemRequest request);
    // Các phương thức khác như updateItem, deleteItem có thể thêm sau
}