package com.example.demo.supplychain.controller;

import com.example.demo.supplychain.entity.GoodsReceipt;
//import com.example.demo.supplychain.entity.GoodsReceiptItem;
import com.example.demo.supplychain.service.GoodsReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goods-receipts")
@RequiredArgsConstructor
public class GoodsReceiptController {

    private final GoodsReceiptService goodsReceiptService;

    @GetMapping
    public ResponseEntity<List<GoodsReceipt>> getAllGoodsReceipts() {
        List<GoodsReceipt> goodsReceipts = goodsReceiptService.getAllGoodsReceipts();
        return ResponseEntity.ok(goodsReceipts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodsReceipt> getGoodsReceiptById(@PathVariable Integer id) {
        try {
            GoodsReceipt goodsReceipt = goodsReceiptService.getGoodsReceiptById(id);
            return ResponseEntity.ok(goodsReceipt);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GoodsReceipt> createGoodsReceipt(@RequestBody CreateGoodsReceiptRequest request) {
        try {
            GoodsReceipt goodsReceipt = goodsReceiptService.createGoodsReceipt(
                request.getPurchaseOrderId(), 
                request.getWarehouseId(), 
                request.getEmployeeId()
            );
            return ResponseEntity.ok(goodsReceipt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    @PostMapping("/{id}/items")
//    public ResponseEntity<GoodsReceiptItem> addItemToGoodsReceipt(
//            @PathVariable Integer id, 
//            @RequestBody AddItemRequest request) {
//        try {
//            GoodsReceiptItem item = goodsReceiptService.addItemToGoodsReceipt(
//                id, 
//                request.getPoItemId(), 
//                request.getQuantityReceived(), 
//                request.getEmployeeId()
//            );
//            return ResponseEntity.ok(item);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeGoodsReceipt(@PathVariable Integer id) {
        try {
            goodsReceiptService.completeGoodsReceipt(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoodsReceipt(@PathVariable Integer id) {
        try {
            goodsReceiptService.deleteGoodsReceipt(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DTOs for request bodies
    public static class CreateGoodsReceiptRequest {
        private Integer purchaseOrderId;
        private Integer warehouseId;
        private Integer employeeId;

        // Getters and setters
        public Integer getPurchaseOrderId() { return purchaseOrderId; }
        public void setPurchaseOrderId(Integer purchaseOrderId) { this.purchaseOrderId = purchaseOrderId; }
        public Integer getWarehouseId() { return warehouseId; }
        public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }
        public Integer getEmployeeId() { return employeeId; }
        public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    }

    public static class AddItemRequest {
        private Integer poItemId;
        private Integer quantityReceived;
        private Integer employeeId;

        // Getters and setters
        public Integer getPoItemId() { return poItemId; }
        public void setPoItemId(Integer poItemId) { this.poItemId = poItemId; }
        public Integer getQuantityReceived() { return quantityReceived; }
        public void setQuantityReceived(Integer quantityReceived) { this.quantityReceived = quantityReceived; }
        public Integer getEmployeeId() { return employeeId; }
        public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    }
}
