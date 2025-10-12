package com.example.demo.product.service;

import com.example.demo.product.dto.request.GoodsReceiptItemRequest;
import com.example.demo.product.dto.response.GoodsReceiptItemResponse;

public interface GoodsReceiptItemService {
    GoodsReceiptItemResponse addItemToGoodsReceipt(Long receiptId, GoodsReceiptItemRequest request);
}