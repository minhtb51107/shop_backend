package com.example.demo.product.service;

import com.example.demo.product.dto.request.CreateInventoryRequest;
import com.example.demo.product.dto.request.UpdateStockRequest;
import com.example.demo.product.dto.response.InventoryResponse;
import java.util.List;

public interface InventoryService {

    InventoryResponse createInventory(CreateInventoryRequest request);

    // Giữ Long warehouseId trong interface
    InventoryResponse getInventory(Long variantId, Long warehouseId);

    List<InventoryResponse> getAllInventories();

    List<InventoryResponse> getInventoriesByVariant(Long variantId);

    // Giữ Long warehouseId trong interface
    List<InventoryResponse> getInventoriesByWarehouse(Long warehouseId);

    InventoryResponse updateStock(UpdateStockRequest request); // UpdateStockRequest đã có warehouseId kiểu Integer, không cần sửa

    /**
     * Kiểm tra xem số lượng tồn kho có đủ cho yêu cầu không.
     * @param variantId ID của biến thể sản phẩm
     * @param warehouseId ID của kho hàng (kiểu Long)
     * @param quantity Số lượng cần kiểm tra
     * @return true nếu đủ hàng, false nếu không đủ hoặc không tìm thấy tồn kho
     */
     // Giữ Long warehouseId trong interface
    boolean checkStockAvailability(Long variantId, Long warehouseId, int quantity);
}