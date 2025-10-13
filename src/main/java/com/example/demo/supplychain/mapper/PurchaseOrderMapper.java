// src/main/java/com/example/demo/supplychain/mapper/PurchaseOrderMapper.java
package com.example.demo.supplychain.mapper;

import com.example.demo.product.mapper.PurchaseOrderItemMapper; // THÊM IMPORT NÀY
import com.example.demo.supplychain.dto.response.PurchaseOrderDetailResponse;
import com.example.demo.supplychain.dto.response.PurchaseOrderSummaryResponse;
import com.example.demo.supplychain.entity.PurchaseOrder;
import com.example.demo.user.entity.Employee;
import lombok.RequiredArgsConstructor; // THÊM IMPORT NÀY
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections; // THÊM IMPORT NÀY
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor // THÊM ANNOTATION NÀY
public class PurchaseOrderMapper {

    // *** INJECT ITEM MAPPER ***
    private final PurchaseOrderItemMapper itemMapper;

    public PurchaseOrderDetailResponse toDetailResponse(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null) {
            return null;
        }

        PurchaseOrderDetailResponse response = new PurchaseOrderDetailResponse();

        response.setId(purchaseOrder.getId());
        response.setOrderDate(purchaseOrder.getOrderDate());
        response.setExpectedDeliveryDate(purchaseOrder.getExpectedDeliveryDate());
        response.setStatus(purchaseOrder.getStatus());

        if (purchaseOrder.getSupplier() != null) {
            response.setSupplierName(purchaseOrder.getSupplier().getName());
        }

        Employee createdBy = purchaseOrder.getCreatedBy();
        if (createdBy != null) {
            response.setCreatedByName(createdBy.getFullname());
        }
        
        // *** THÊM LOGIC MAPPING ITEMS ***
        if (purchaseOrder.getItems() != null) {
            response.setItems(
                purchaseOrder.getItems().stream()
                    .map(itemMapper::toResponse)
                    .collect(Collectors.toList())
            );
        } else {
            response.setItems(Collections.emptyList());
        }

        return response;
    }
    
    // Phương thức toSummaryResponse và toSummaryResponseList giữ nguyên
    public PurchaseOrderSummaryResponse toSummaryResponse(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null) {
            return null;
        }

        PurchaseOrderSummaryResponse summary = new PurchaseOrderSummaryResponse();

        summary.setId(purchaseOrder.getId());
        summary.setOrderDate(purchaseOrder.getOrderDate());
        summary.setStatus(purchaseOrder.getStatus());

        if (purchaseOrder.getSupplier() != null) {
            summary.setSupplierName(purchaseOrder.getSupplier().getName());
        }

        return summary;
    }
    
    public List<PurchaseOrderSummaryResponse> toSummaryResponseList(List<PurchaseOrder> purchaseOrders) {
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return new ArrayList<>();
        }

        return purchaseOrders.stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }
}