package com.example.demo.product.controller;


import com.example.demo.product.dto.request.GoodsReceiptItemRequest;
import com.example.demo.product.dto.response.GoodsReceiptItemResponse;
import com.example.demo.product.service.GoodsReceiptItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goods-receipts") // Base path là của "cha"
public class GoodsReceiptItemController {

    private final GoodsReceiptItemService itemService;

    public GoodsReceiptItemController(GoodsReceiptItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/{receiptId}/items")
    public ResponseEntity<GoodsReceiptItemResponse> addItemToReceipt(
            @PathVariable Long receiptId,
            @RequestBody GoodsReceiptItemRequest request) {
        GoodsReceiptItemResponse response = itemService.addItemToGoodsReceipt(receiptId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}