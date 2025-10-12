package com.example.demo.supplychain.service;

import com.example.demo.supplychain.dto.request.CreatePurchaseOrderRequest;
import com.example.demo.supplychain.dto.response.PurchaseOrderDetailResponse;
import com.example.demo.supplychain.dto.response.PurchaseOrderSummaryResponse;
import com.example.demo.supplychain.enums.PurchaseOrderStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseOrderService {

    PurchaseOrderDetailResponse createPurchaseOrder(CreatePurchaseOrderRequest request);

    PurchaseOrderDetailResponse findPurchaseOrderById(Integer id);

 // Thay thế List<PurchaseOrderSummaryResponse> findAllPurchaseOrders(); bằng:
    Page<PurchaseOrderSummaryResponse> findAllPurchaseOrders(Pageable pageable, PurchaseOrderStatus status);
 // ... các phương thức cũ ...
    PurchaseOrderDetailResponse approvePurchaseOrder(Integer id);
    
    PurchaseOrderDetailResponse cancelPurchaseOrder(Integer id);
}