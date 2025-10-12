// File: mapper/GoodsReceiptItemMapper.java
package com.example.demo.product.mapper;


import com.example.demo.product.dto.response.GoodsReceiptItemResponse;
import com.example.demo.product.entity.GoodsReceiptItem;
import org.springframework.stereotype.Component;

@Component
public class GoodsReceiptItemMapper {
    public GoodsReceiptItemResponse toResponse(GoodsReceiptItem entity) {
        if (entity == null) return null;
        GoodsReceiptItemResponse dto = new GoodsReceiptItemResponse();
        dto.setId(entity.getId());
        dto.setQuantityReceived(entity.getQuantityReceived());
        dto.setUnitCost(entity.getUnitCost());
        if (entity.getGoodsReceipt() != null) {
            dto.setGoodsReceiptId(entity.getGoodsReceipt().getId().longValue());
        }
        if (entity.getVariant() != null) {
            dto.setVariantId(entity.getVariant().getId());
            dto.setVariantSku(entity.getVariant().getSku());
        }
        return dto;
    }
}