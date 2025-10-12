package com.example.demo.supplychain.mapper;

import com.example.demo.supplychain.dto.response.PurchaseOrderDetailResponse;
import com.example.demo.supplychain.dto.response.PurchaseOrderSummaryResponse;
import com.example.demo.supplychain.entity.PurchaseOrder;
import com.example.demo.user.entity.Employee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component // Thay thế @Mapper(componentModel = "spring")
public class PurchaseOrderMapper { // Chuyển từ "interface" thành "class"

    // Bỏ @Mapping và viết code xử lý trực tiếp
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

        return response;
    }

    // Bỏ @Mapping và viết code xử lý trực tiếp
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

    // Viết code xử lý trực tiếp
    public List<PurchaseOrderSummaryResponse> toSummaryResponseList(List<PurchaseOrder> purchaseOrders) {
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return new ArrayList<>();
        }

        return purchaseOrders.stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }
}