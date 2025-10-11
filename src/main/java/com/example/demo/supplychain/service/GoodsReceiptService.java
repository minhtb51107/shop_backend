package com.example.demo.supplychain.service;

import com.example.demo.supplychain.entity.GoodsReceipt;
import com.example.demo.supplychain.repository.GoodsReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoodsReceiptService {

    private final GoodsReceiptRepository goodsReceiptRepository;

    public List<GoodsReceipt> findAllGoodsReceipts() {
        return goodsReceiptRepository.findAll();
    }

    public Optional<GoodsReceipt> findGoodsReceiptById(Integer id) {
        return goodsReceiptRepository.findById(id);
    }

    public GoodsReceipt saveGoodsReceipt(GoodsReceipt goodsReceipt) {
        return goodsReceiptRepository.save(goodsReceipt);
    }

    public void deleteGoodsReceipt(Integer id) {
        goodsReceiptRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return goodsReceiptRepository.existsById(id);
    }
}
