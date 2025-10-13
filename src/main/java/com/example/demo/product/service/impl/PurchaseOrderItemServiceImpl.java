// src/main/java/com/example/demo/product/service/impl/PurchaseOrderItemServiceImpl.java
package com.example.demo.product.service.impl;


import com.example.demo.product.dto.request.PurchaseOrderItemRequest;
import com.example.demo.product.dto.response.PurchaseOrderItemResponse;
import com.example.demo.product.entity.ProductVariant;
import com.example.demo.product.entity.PurchaseOrderItem;
import com.example.demo.product.mapper.PurchaseOrderItemMapper;
import com.example.demo.product.repository.PurchaseOrderItemRepository;
import com.example.demo.product.repository.VariantRepository;
import com.example.demo.product.service.PurchaseOrderItemService;
import com.example.demo.shared.exception.BadRequestException; // THÊM IMPORT NÀY
import com.example.demo.supplychain.entity.PurchaseOrder;
import com.example.demo.supplychain.enums.PurchaseOrderStatus; // THÊM IMPORT NÀY
import com.example.demo.supplychain.repository.PurchaseOrderRepository;
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
    public PurchaseOrderItemResponse addItemToPurchaseOrder(Integer orderId, PurchaseOrderItemRequest request) {
        PurchaseOrder po = poRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("PurchaseOrder not found with id: " + orderId));

        // *** KIỂM TRA TRẠNG THÁI ĐƠN HÀNG ***
        if (po.getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new BadRequestException("Chỉ có thể thêm sản phẩm vào đơn hàng ở trạng thái 'Nháp' (DRAFT).");
        }

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
    
    // *** IMPLEMENT PHƯƠNG THỨC UPDATE ***
    @Override
    @Transactional
    public PurchaseOrderItemResponse updateItem(Long itemId, PurchaseOrderItemRequest request) {
        PurchaseOrderItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("PurchaseOrderItem not found with id: " + itemId));

        if (item.getPurchaseOrder().getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new BadRequestException("Chỉ có thể sửa sản phẩm khi đơn hàng ở trạng thái 'Nháp' (DRAFT).");
        }
        
        // Cập nhật các trường cho phép
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());

        // Không cho phép thay đổi variantId, nếu cần thì phải xóa đi thêm lại.

        PurchaseOrderItem updatedItem = itemRepository.save(item);
        return itemMapper.toResponse(updatedItem);
    }

    // *** IMPLEMENT PHƯƠNG THỨC DELETE ***
    @Override
    @Transactional
    public void deleteItem(Long itemId) {
        PurchaseOrderItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("PurchaseOrderItem not found with id: " + itemId));
        
        if (item.getPurchaseOrder().getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new BadRequestException("Chỉ có thể xóa sản phẩm khi đơn hàng ở trạng thái 'Nháp' (DRAFT).");
        }

        itemRepository.delete(item);
    }
}