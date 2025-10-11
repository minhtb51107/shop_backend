// File: mapper/PurchaseOrderItemMapper.java
package com.example.demo.product_inventory.mapper;

import com.example.demo.product_inventory.dto.response.PurchaseOrderItemResponse;
import com.example.demo.product_inventory.entity.PurchaseOrderItem;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderItemMapper {
    public PurchaseOrderItemResponse toResponse(PurchaseOrderItem entity) {
        if (entity == null) return null;
        PurchaseOrderItemResponse dto = new PurchaseOrderItemResponse();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        if (entity.getVariant() != null) {
            dto.setVariantId(entity.getVariant().getId());
            dto.setVariantSku(entity.getVariant().getSku());
        }
        return dto;
    }
}