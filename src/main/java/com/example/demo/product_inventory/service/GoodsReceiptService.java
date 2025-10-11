// File: service/GoodsReceiptService.java
package com.example.demo.product_inventory.service;

import com.example.demo.product_inventory.dto.request.GoodsReceiptRequest;
import com.example.demo.product_inventory.dto.response.GoodsReceiptResponse;
import java.util.Optional;

public interface GoodsReceiptService {
    GoodsReceiptResponse createGoodsReceipt(GoodsReceiptRequest request);
    Optional<GoodsReceiptResponse> findById(Long id);
}