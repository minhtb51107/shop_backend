package com.example.demo.product_inventory.service.impl;

import com.example.demo.product_inventory.dto.request.PurchaseOrderRequest;
import com.example.demo.product_inventory.dto.response.PurchaseOrderResponse;
import com.example.demo.product_inventory.entity.*;
import com.example.demo.product_inventory.mapper.PurchaseOrderMapper;
import com.example.demo.product_inventory.repository.*;
import com.example.demo.product_inventory.service.PurchaseOrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate; // SỬA LẠI
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository poRepository;
    private final SupplierRepository supplierRepository;
    private final VariantRepository variantRepository;
    private final PurchaseOrderMapper poMapper;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository poRepository, SupplierRepository supplierRepository, VariantRepository variantRepository, PurchaseOrderMapper poMapper) {
        this.poRepository = poRepository;
        this.supplierRepository = supplierRepository;
        this.variantRepository = variantRepository;
        this.poMapper = poMapper;
    }

    @Override
    @Transactional
    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + request.getSupplierId()));

        PurchaseOrder po = new PurchaseOrder();
        po.setSupplier(supplier);
        po.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        po.setCreatedByEmployeeId(request.getCreatedByEmployeeId());
        po.setOrderDate(LocalDate.now()); // SỬA LẠI
        po.setStatus(PurchaseOrder.POStatus.DRAFT);

        List<PurchaseOrderItem> items = new ArrayList<>();
        if (request.getItems() != null) {
            for(var itemRequest : request.getItems()) {
                ProductVariant variant = variantRepository.findById(itemRequest.getVariantId())
                        .orElseThrow(() -> new EntityNotFoundException("Variant not found with id: " + itemRequest.getVariantId()));
                PurchaseOrderItem item = new PurchaseOrderItem();
                item.setPurchaseOrder(po);
                item.setVariant(variant);
                item.setQuantity(itemRequest.getQuantity());
                item.setUnitPrice(itemRequest.getUnitPrice());
                items.add(item);
            }
        }
        po.setItems(items);

        PurchaseOrder savedPo = poRepository.save(po);
        return poMapper.toResponse(savedPo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrderResponse> findById(Long id) {
        return poRepository.findById(id).map(poMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> findAll() {
        return poRepository.findAll().stream()
                .map(poMapper::toResponse)
                .collect(Collectors.toList());
    }
}