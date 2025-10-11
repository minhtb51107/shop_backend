package com.example.demo.product_inventory.mapper;

import com.example.demo.product_inventory.dto.response.PurchaseOrderResponse;
import com.example.demo.product_inventory.entity.PurchaseOrder;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class PurchaseOrderMapper {

    private final PurchaseOrderItemMapper itemMapper;

    public PurchaseOrderMapper(PurchaseOrderItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public PurchaseOrderResponse toResponse(PurchaseOrder entity) {
        if (entity == null) return null;
        PurchaseOrderResponse dto = new PurchaseOrderResponse();
        dto.setId(entity.getId());
        // Bây giờ sẽ hoạt động!
        dto.setOrderDate(entity.getOrderDate());
        dto.setExpectedDeliveryDate(entity.getExpectedDeliveryDate());
        dto.setStatus(entity.getStatus());
        dto.setCreatedByEmployeeId(entity.getCreatedByEmployeeId());

        if (entity.getSupplier() != null) {
            dto.setSupplierId(entity.getSupplier().getId());
            dto.setSupplierName(entity.getSupplier().getName());
        }

        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(itemMapper::toResponse)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}