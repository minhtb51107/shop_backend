package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.GoodsReceiptItemRequest;
import com.example.demo.product.dto.response.GoodsReceiptItemResponse;
import com.example.demo.supplychain.entity.GoodsReceipt;
import com.example.demo.product.entity.GoodsReceiptItem;
import com.example.demo.product.entity.ProductVariant;
import com.example.demo.product.mapper.GoodsReceiptItemMapper;
import com.example.demo.product.repository.GoodsReceiptItemRepository;
import com.example.demo.product.repository.VariantRepository;
import com.example.demo.product.service.GoodsReceiptItemService;
import com.example.demo.supplychain.repository.GoodsReceiptRepository; // Sử dụng repo từ supplychain
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsReceiptItemServiceImpl implements GoodsReceiptItemService {

    private final GoodsReceiptItemRepository itemRepository;
    private final GoodsReceiptRepository grRepository;
    private final VariantRepository variantRepository;
    private final GoodsReceiptItemMapper itemMapper;

    public GoodsReceiptItemServiceImpl(GoodsReceiptItemRepository itemRepository, GoodsReceiptRepository grRepository, VariantRepository variantRepository, GoodsReceiptItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.grRepository = grRepository;
        this.variantRepository = variantRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional
    public GoodsReceiptItemResponse addItemToGoodsReceipt(Long receiptId, GoodsReceiptItemRequest request) {
        GoodsReceipt gr = grRepository.findById(receiptId.intValue())
                .orElseThrow(() -> new EntityNotFoundException("GoodsReceipt not found with id: " + receiptId));

        ProductVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found with id: " + request.getVariantId()));

        GoodsReceiptItem newItem = new GoodsReceiptItem();
        newItem.setGoodsReceipt(gr);
        newItem.setVariant(variant);
        newItem.setQuantityReceived(request.getQuantityReceived());
        newItem.setUnitCost(request.getUnitCost());
        newItem.setPurchaseOrderItemId(request.getPurchaseOrderItemId());

        GoodsReceiptItem savedItem = itemRepository.save(newItem);
        // DB TRIGGER SẼ TỰ ĐỘNG CẬP NHẬT TỒN KHO TẠI ĐÂY
        return itemMapper.toResponse(savedItem);
    }
}