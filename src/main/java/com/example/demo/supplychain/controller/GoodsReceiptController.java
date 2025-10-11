package com.example.demo.supplychain.controller;

import com.example.demo.supplychain.entity.GoodsReceipt;
import com.example.demo.supplychain.repository.GoodsReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/goods-receipts")
@RequiredArgsConstructor
public class GoodsReceiptController {

    private final GoodsReceiptRepository goodsReceiptRepository;

    @GetMapping
    public ResponseEntity<List<GoodsReceipt>> getAllGoodsReceipts() {
        List<GoodsReceipt> goodsReceipts = goodsReceiptRepository.findAll();
        return ResponseEntity.ok(goodsReceipts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodsReceipt> getGoodsReceiptById(@PathVariable Integer id) {
        Optional<GoodsReceipt> goodsReceipt = goodsReceiptRepository.findById(id);
        return goodsReceipt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GoodsReceipt> createGoodsReceipt(@RequestBody GoodsReceipt goodsReceipt) {
        GoodsReceipt savedGoodsReceipt = goodsReceiptRepository.save(goodsReceipt);
        return ResponseEntity.ok(savedGoodsReceipt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoodsReceipt> updateGoodsReceipt(@PathVariable Integer id, @RequestBody GoodsReceipt goodsReceipt) {
        if (!goodsReceiptRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        goodsReceipt.setId(id);
        GoodsReceipt updatedGoodsReceipt = goodsReceiptRepository.save(goodsReceipt);
        return ResponseEntity.ok(updatedGoodsReceipt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoodsReceipt(@PathVariable Integer id) {
        if (!goodsReceiptRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        goodsReceiptRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
