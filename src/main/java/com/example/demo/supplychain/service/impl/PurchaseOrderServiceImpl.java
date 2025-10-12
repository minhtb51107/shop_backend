// File: src/main/java/com/example/demo/supplychain/service/impl/PurchaseOrderServiceImpl.java
package com.example.demo.supplychain.service.impl;

import com.example.demo.supplychain.dto.request.CreatePurchaseOrderRequest;
import com.example.demo.supplychain.dto.response.PurchaseOrderDetailResponse;
import com.example.demo.supplychain.dto.response.PurchaseOrderSummaryResponse;
import com.example.demo.supplychain.entity.*;
import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import com.example.demo.supplychain.mapper.PurchaseOrderMapper;
import com.example.demo.supplychain.repository.PurchaseOrderRepository;
import com.example.demo.supplychain.repository.SupplierRepository;
import com.example.demo.supplychain.repository.EmployeeRepository;
import com.example.demo.supplychain.repository.ProductVariantRepository;
import com.example.demo.supplychain.security.CurrentUserService;
import com.example.demo.supplychain.service.PurchaseOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepo;
    private final SupplierRepository supplierRepo;
    private final EmployeeRepository employeeRepo;
    private final ProductVariantRepository productVariantRepo;
    private final CurrentUserService currentUserService;
    private final PurchaseOrderMapper purchaseOrderMapper; // Dùng Mapper để chuyển đổi

    @Override
    @Transactional
    public PurchaseOrderDetailResponse createPurchaseOrder(CreatePurchaseOrderRequest request) {
        // ... (Logic tạo PurchaseOrder và PurchaseOrderItem như cũ)
        Supplier supplier = supplierRepo.findById(request.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));
        
        Employee employee = currentUserService.getCurrentEmployee(); // Lấy employee hiện tại từ security context

        PurchaseOrder po = PurchaseOrder.builder()
                .supplier(supplier)
                .orderDate(LocalDate.now())
                .expectedDeliveryDate(request.getExpectedDeliveryDate())
                .status(PurchaseOrderStatus.DRAFT)
                .createdBy(employee) // Sử dụng relationship thực sự
                .build();

        List<PurchaseOrderItem> items = request.getItems().stream().map(itemDto -> {
            ProductVariant variant = productVariantRepo.findById(itemDto.getVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product Variant not found"));
            
            return PurchaseOrderItem.builder()
                    .purchaseOrder(po)
                    .variant(variant) // Sử dụng relationship thực sự
                    .quantity(itemDto.getQuantity())
                    .unitPrice(itemDto.getUnitPrice())
                    .build();
        }).collect(Collectors.toList());
        po.setItems(items);

        PurchaseOrder savedPo = purchaseOrderRepo.save(po);

        // Dùng mapper để chuyển đổi Entity đã lưu thành DTO và trả về
        return purchaseOrderMapper.toDetailResponse(savedPo);
    }

    @Override
    @Transactional(readOnly = true) // readOnly = true để tối ưu cho các tác vụ chỉ đọc
    public PurchaseOrderDetailResponse findPurchaseOrderById(Integer id) {
        PurchaseOrder po = purchaseOrderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase Order not found with id: " + id));
        return purchaseOrderMapper.toDetailResponse(po);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderSummaryResponse> findAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepo.findAll();
        return purchaseOrderMapper.toSummaryResponseList(purchaseOrders);
    }
}