// File: mapper/GoodsReceiptMapper.java
package com.example.demo.product_inventory.mapper;

import com.example.demo.product_inventory.dto.response.GoodsReceiptResponse;
import com.example.demo.product_inventory.entity.GoodsReceipt;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class GoodsReceiptMapper {

    private final GoodsReceiptItemMapper itemMapper;

    public GoodsReceiptMapper(GoodsReceiptItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public GoodsReceiptResponse toResponse(GoodsReceipt entity) {
        if (entity == null) return null;
        GoodsReceiptResponse dto = new GoodsReceiptResponse();
        dto.setId(entity.getId());
        dto.setReceiptDate(entity.getReceiptDate());
        dto.setCreatedByEmployeeId(entity.getCreatedByEmployeeId());

        if (entity.getPurchaseOrder() != null) {
            dto.setPurchaseOrderId(entity.getPurchaseOrder().getId());
        }
        if (entity.getWarehouse() != null) {
            dto.setWarehouseId(entity.getWarehouse().getId());
            dto.setWarehouseName(entity.getWarehouse().getName());
        }
        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(itemMapper::toResponse)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}