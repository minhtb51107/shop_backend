package com.example.demo.product.service.impl;


import com.example.demo.product.dto.request.PurchaseOrderItemRequest;
import com.example.demo.product.dto.response.PurchaseOrderItemResponse;
import com.example.demo.supplychain.entity.PurchaseOrder;
import com.example.demo.product.entity.PurchaseOrderItem;
import com.example.demo.product.entity.ProductVariant;
import com.example.demo.product.mapper.PurchaseOrderItemMapper;
import com.example.demo.product.repository.PurchaseOrderItemRepository;
import com.example.demo.product.repository.VariantRepository;
import com.example.demo.product.service.PurchaseOrderItemService;
import com.example.demo.supplychain.repository.PurchaseOrderRepository; // Sử dụng repo từ supplychain
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {

    private final PurchaseOrderItemRepository itemRepository;
    private final PurchaseOrderRepository poRepository;
    private final VariantRepository variantRepository;
    private final PurchaseOrderItemMapper itemMapper;

    public PurchaseOrderItemServiceImpl(PurchaseOrderItemRepository itemRepository, PurchaseOrderRepository poRepository, VariantRepository variantRepository, PurchaseOrderItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.poRepository = poRepository;
        this.variantRepository = variantRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional
    public PurchaseOrderItemResponse addItemToPurchaseOrder(Long orderId, PurchaseOrderItemRequest request) {
        PurchaseOrder po = poRepository.findById(orderId.intValue())
                .orElseThrow(() -> new EntityNotFoundException("PurchaseOrder not found with id: " + orderId));

        ProductVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found with id: " + request.getVariantId()));

        PurchaseOrderItem newItem = new PurchaseOrderItem();
        newItem.setPurchaseOrder(po);
        newItem.setVariant(variant);
        newItem.setQuantity(request.getQuantity());
        newItem.setUnitPrice(request.getUnitPrice());

        PurchaseOrderItem savedItem = itemRepository.save(newItem);
        return itemMapper.toResponse(savedItem);
    }
}