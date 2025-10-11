// File: src/main/java/com/example/demo/user/mapper/PurchaseOrderMapper.java
package com.example.demo.supplychain.mapper;

import com.example.demo.supplychain.dto.response.PurchaseOrderDetailResponse;
import com.example.demo.supplychain.dto.response.PurchaseOrderSummaryResponse;
import com.example.demo.supplychain.entity.PurchaseOrder;
import com.example.demo.supplychain.entity.PurchaseOrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "createdBy.fullname", target = "createdByName")
    @Mapping(target = "grandTotal", ignore = true)
    PurchaseOrderDetailResponse toDetailResponse(PurchaseOrder purchaseOrder);

    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(target = "grandTotal", ignore = true)
    PurchaseOrderSummaryResponse toSummaryResponse(PurchaseOrder purchaseOrder);

    List<PurchaseOrderSummaryResponse> toSummaryResponseList(List<PurchaseOrder> purchaseOrders);

    @Mapping(source = "id", target = "itemId")
    @Mapping(source = "variant.id", target = "variantId")
    @Mapping(source = "variant.sku", target = "variantSku")
    @Mapping(source = "variant.product.name", target = "variantName")
    @Mapping(target = "totalPrice", ignore = true)
    PurchaseOrderDetailResponse.ItemResponse itemToItemResponse(PurchaseOrderItem item);

    @AfterMapping
    default void calculateTotals(@MappingTarget PurchaseOrderDetailResponse dto, PurchaseOrder po) {
        if (dto.getItems() != null && po.getItems() != null) {
            for (int i = 0; i < dto.getItems().size(); i++) {
                PurchaseOrderDetailResponse.ItemResponse itemDto = dto.getItems().get(i);
                if(itemDto.getQuantity() != null && itemDto.getUnitPrice() != null) {
                    itemDto.setTotalPrice(itemDto.getUnitPrice().multiply(new BigDecimal(itemDto.getQuantity())));
                }
            }
        }

        BigDecimal grandTotal = po.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setGrandTotal(grandTotal);
    }

    @AfterMapping
    default void calculateGrandTotalForSummary(@MappingTarget PurchaseOrderSummaryResponse dto, PurchaseOrder po) {
        BigDecimal grandTotal = po.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setGrandTotal(grandTotal);
    }
}