package com.example.demo.supplychain.service;

import com.example.demo.supplychain.entity.GoodsReceiptItem;
import com.example.demo.supplychain.repository.GoodsReceiptItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoodsReceiptItemService {

    private final GoodsReceiptItemRepository goodsReceiptItemRepository;

    public List<GoodsReceiptItem> findAllGoodsReceiptItems() {
        return goodsReceiptItemRepository.findAll();
    }

    public Optional<GoodsReceiptItem> findGoodsReceiptItemById(Integer id) {
        return goodsReceiptItemRepository.findById(id);
    }

    public GoodsReceiptItem saveGoodsReceiptItem(GoodsReceiptItem goodsReceiptItem) {
        return goodsReceiptItemRepository.save(goodsReceiptItem);
    }

    public void deleteGoodsReceiptItem(Integer id) {
        goodsReceiptItemRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return goodsReceiptItemRepository.existsById(id);
    }
}
