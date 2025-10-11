// File: service/impl/GoodsReceiptServiceImpl.java
package com.example.demo.product_inventory.service.impl;

import com.example.demo.product_inventory.dto.request.GoodsReceiptRequest;
import com.example.demo.product_inventory.dto.response.GoodsReceiptResponse;
import com.example.demo.product_inventory.entity.*;
import com.example.demo.product_inventory.mapper.GoodsReceiptMapper;
import com.example.demo.product_inventory.repository.*;
import com.example.demo.product_inventory.service.GoodsReceiptService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GoodsReceiptServiceImpl implements GoodsReceiptService {

    private final GoodsReceiptRepository grRepository;
    private final PurchaseOrderRepository poRepository;
    private final WarehouseRepository warehouseRepository;
    private final VariantRepository variantRepository;
    private final GoodsReceiptMapper grMapper;

    public GoodsReceiptServiceImpl(GoodsReceiptRepository grRepository, PurchaseOrderRepository poRepository, WarehouseRepository warehouseRepository, VariantRepository variantRepository, GoodsReceiptMapper grMapper) {
        this.grRepository = grRepository;
        this.poRepository = poRepository;
        this.warehouseRepository = warehouseRepository;
        this.variantRepository = variantRepository;
        this.grMapper = grMapper;
    }

    @Override
    @Transactional
    public GoodsReceiptResponse createGoodsReceipt(GoodsReceiptRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));

        GoodsReceipt gr = new GoodsReceipt();
        gr.setWarehouse(warehouse);
        gr.setCreatedByEmployeeId(request.getCreatedByEmployeeId());

        if (request.getPurchaseOrderId() != null) {
            PurchaseOrder po = poRepository.findById(request.getPurchaseOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("Purchase Order not found"));
            gr.setPurchaseOrder(po);
        }

        List<GoodsReceiptItem> items = new ArrayList<>();
        for (var itemRequest : request.getItems()) {
            ProductVariant variant = variantRepository.findById(itemRequest.getVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Variant not found"));

            GoodsReceiptItem item = new GoodsReceiptItem();
            item.setGoodsReceipt(gr); // Link item back to the receipt
            item.setVariant(variant);
            item.setQuantityReceived(itemRequest.getQuantityReceived());
            item.setUnitCost(itemRequest.getUnitCost());
            item.setPurchaseOrderItemId(itemRequest.getPurchaseOrderItemId());
            items.add(item);
        }
        gr.setItems(items);

        GoodsReceipt savedGr = grRepository.save(gr);
        // DB TRIGGER WILL UPDATE INVENTORY AUTOMATICALLY HERE
        return grMapper.toResponse(savedGr);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GoodsReceiptResponse> findById(Long id) {
        return grRepository.findById(id).map(grMapper::toResponse);
    }
}