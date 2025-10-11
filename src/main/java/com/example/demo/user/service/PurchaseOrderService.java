package com.example.demo.user.service;

import com.example.demo.user.dto.request.CreatePurchaseOrderRequest;
import com.example.demo.user.dto.response.PurchaseOrderDetailResponse;
import com.example.demo.user.dto.response.PurchaseOrderSummaryResponse;

import java.util.List;

public interface PurchaseOrderService {

    /**
     * Tạo một đơn đặt hàng mới và trả về thông tin chi tiết của đơn vừa tạo.
     */
    PurchaseOrderDetailResponse createPurchaseOrder(CreatePurchaseOrderRequest request);

    /**
     * Lấy thông tin chi tiết của một đơn đặt hàng theo ID.
     */
    PurchaseOrderDetailResponse findPurchaseOrderById(Long id);

    /**
     * Lấy danh sách tóm tắt tất cả các đơn đặt hàng.
     */
    List<PurchaseOrderSummaryResponse> findAllPurchaseOrders();
}