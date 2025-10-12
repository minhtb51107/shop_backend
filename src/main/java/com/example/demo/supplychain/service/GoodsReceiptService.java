package com.example.demo.supplychain.service;

import com.example.demo.supplychain.entity.GoodsReceipt;
import com.example.demo.supplychain.entity.GoodsReceiptItem;

import java.util.List;

public interface GoodsReceiptService {
    
    List<GoodsReceipt> getAllGoodsReceipts();
    
    GoodsReceipt getGoodsReceiptById(Integer id);
    
    GoodsReceipt createGoodsReceipt(Integer purchaseOrderId, Integer warehouseId, Integer employeeId);
    
    GoodsReceiptItem addItemToGoodsReceipt(Integer goodsReceiptId, Integer poItemId, 
                                          Integer quantityReceived, Integer employeeId);
    
    void completeGoodsReceipt(Integer goodsReceiptId);
    
    void deleteGoodsReceipt(Integer id);
}
