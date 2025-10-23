package com.example.demo.product.controller;

import com.example.demo.product.dto.request.CreateInventoryRequest;
import com.example.demo.product.dto.request.UpdateStockRequest; // Đảm bảo DTO này đã được sửa đúng
import com.example.demo.product.dto.response.InventoryResponse;
import com.example.demo.product.service.InventoryService;
import jakarta.validation.Valid; // Thêm import validation
import lombok.RequiredArgsConstructor; // Sử dụng Lombok constructor
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor // Dùng Lombok để inject dependency
public class InventoryController {

    private final InventoryService inventoryService;

    // GET /api/v1/inventory - Lấy tất cả tồn kho
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventories() { // Sửa tên phương thức controller
        // Sửa tên phương thức service được gọi
        List<InventoryResponse> inventoryList = inventoryService.getAllInventories();
        return ResponseEntity.ok(inventoryList);
    }

    // GET /api/v1/inventory/{variantId}/{warehouseId} - Lấy tồn kho cụ thể
    @GetMapping("/{variantId}/{warehouseId}")
    public ResponseEntity<InventoryResponse> getInventoryById(
            @PathVariable Long variantId,
            @PathVariable Long warehouseId) { // Đổi warehouseId thành Long
        // Gọi service trực tiếp, exception sẽ được global handler xử lý
        InventoryResponse inventory = inventoryService.getInventory(variantId, warehouseId);
        return ResponseEntity.ok(inventory);
    }

    // POST /api/v1/inventory - Tạo bản ghi tồn kho mới
    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody CreateInventoryRequest request) { // Thêm @Valid
        InventoryResponse newInventory = inventoryService.createInventory(request);
        return new ResponseEntity<>(newInventory, HttpStatus.CREATED);
    }

    // PUT /api/v1/inventory/stock - Cập nhật số lượng tồn kho
    @PutMapping("/stock") // Thay đổi đường dẫn và bỏ PathVariable
    public ResponseEntity<InventoryResponse> updateInventoryStock(
            @Valid @RequestBody UpdateStockRequest request) { // Chỉ cần RequestBody chứa đủ thông tin
        // Gọi service trực tiếp, exception sẽ được global handler xử lý
        InventoryResponse updatedInventory = inventoryService.updateStock(request);
        return ResponseEntity.ok(updatedInventory);
    }

    /*
    // DELETE /api/v1/inventory/{variantId}/{warehouseId} - Xóa bản ghi tồn kho
    // Tạm thời xóa bỏ vì chưa có trong service interface
    @DeleteMapping("/{variantId}/{warehouseId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long variantId, @PathVariable Long warehouseId) { // Đổi warehouseId thành Long
        boolean isDeleted = inventoryService.deleteInventory(variantId, warehouseId); // Giả sử service có hàm này
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            // Service nên ném ResourceNotFoundException nếu không tìm thấy
            return ResponseEntity.notFound().build();
        }
    }
    */
}