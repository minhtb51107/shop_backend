//package com.example.demo.supplychain.controller;
//
//import com.example.demo.supplychain.entity.GoodsReceiptItem;
//import com.example.demo.supplychain.repository.GoodsReceiptItemRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/v1/goods-receipt-items")
//@RequiredArgsConstructor
//public class GoodsReceiptItemController {
//
//    private final GoodsReceiptItemRepository goodsReceiptItemRepository;
//
//    @GetMapping
//    public ResponseEntity<List<GoodsReceiptItem>> getAllGoodsReceiptItems() {
//        List<GoodsReceiptItem> goodsReceiptItems = goodsReceiptItemRepository.findAll();
//        return ResponseEntity.ok(goodsReceiptItems);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<GoodsReceiptItem> getGoodsReceiptItemById(@PathVariable Integer id) {
//        Optional<GoodsReceiptItem> goodsReceiptItem = goodsReceiptItemRepository.findById(id);
//        return goodsReceiptItem.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public ResponseEntity<GoodsReceiptItem> createGoodsReceiptItem(@RequestBody GoodsReceiptItem goodsReceiptItem) {
//        GoodsReceiptItem savedGoodsReceiptItem = goodsReceiptItemRepository.save(goodsReceiptItem);
//        return ResponseEntity.ok(savedGoodsReceiptItem);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<GoodsReceiptItem> updateGoodsReceiptItem(@PathVariable Integer id, @RequestBody GoodsReceiptItem goodsReceiptItem) {
//        if (!goodsReceiptItemRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        goodsReceiptItem.setId(id);
//        GoodsReceiptItem updatedGoodsReceiptItem = goodsReceiptItemRepository.save(goodsReceiptItem);
//        return ResponseEntity.ok(updatedGoodsReceiptItem);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteGoodsReceiptItem(@PathVariable Integer id) {
//        if (!goodsReceiptItemRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        goodsReceiptItemRepository.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}
