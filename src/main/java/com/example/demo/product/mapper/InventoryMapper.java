package com.example.demo.product.mapper;

import com.example.demo.product.dto.response.InventoryResponse;
import com.example.demo.product.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryResponse toResponse(Inventory entity) {
        if (entity == null) {
            return null;
        }

        InventoryResponse response = new InventoryResponse();
        response.setVariantId(entity.getId().getVariantId());
        response.setWarehouseId(entity.getId().getWarehouseId());
        response.setStockQuantity(entity.getStockQuantity());

        if (entity.getVariant() != null) {
            response.setVariantSku(entity.getVariant().getSku());
            response.setVariantPrice(entity.getVariant().getPrice());
        }

        if (entity.getWarehouse() != null) {
            response.setWarehouseName(entity.getWarehouse().getName());
            response.setWarehouseLocation(entity.getWarehouse().getAddress());
        }

        return response;
    }
}