package com.example.demo.user.mapper;

import com.example.demo.user.dto.request.InventoryRequest;
import com.example.demo.user.dto.response.InventoryResponse;
import com.example.demo.user.entity.Inventory;

public class InventoryMapper {

    public static Inventory toEntity(InventoryRequest req) {
        Inventory inv = new Inventory();
        inv.setStockQuantity(req.getStockQuantity());
        return inv;
    }

    public static InventoryResponse toResponse(Inventory entity) {
        InventoryResponse res = new InventoryResponse();
        if (entity.getVariant() != null) {
            res.setVariantId(entity.getVariant().getId());
            if (entity.getVariant().getProduct() != null) {
                res.setProductName(entity.getVariant().getProduct().getName());
            }
        }
        if (entity.getWarehouse() != null) {
            res.setWarehouseId(entity.getWarehouse().getId());
            res.setWarehouseName(entity.getWarehouse().getName());
        }
        res.setStockQuantity(entity.getStockQuantity());
        return res;
    }
}
