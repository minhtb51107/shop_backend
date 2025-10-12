package com.example.demo.supplychain.service.impl;

import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException; // Thay thế jakarta...
import com.example.demo.supplychain.dto.request.CreatePurchaseOrderRequest;
import com.example.demo.supplychain.dto.response.PurchaseOrderDetailResponse;
import com.example.demo.supplychain.dto.response.PurchaseOrderSummaryResponse;
import com.example.demo.supplychain.entity.PurchaseOrder;
import com.example.demo.supplychain.entity.Supplier;
import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import com.example.demo.supplychain.mapper.PurchaseOrderMapper;
import com.example.demo.supplychain.repository.PurchaseOrderRepository;
import com.example.demo.supplychain.repository.SupplierRepository;
import com.example.demo.supplychain.security.CurrentUserService;
import com.example.demo.supplychain.service.PurchaseOrderService;
import com.example.demo.user.entity.Employee;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepo;
    private final SupplierRepository supplierRepo;
    private final CurrentUserService currentUserService; // Dùng để lấy nhân viên đang đăng nhập
    private final PurchaseOrderMapper purchaseOrderMapper;

    @Override
    @Transactional
    public PurchaseOrderDetailResponse createPurchaseOrder(CreatePurchaseOrderRequest request) {
        Supplier supplier = supplierRepo.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp với ID: " + request.getSupplierId()));

        Employee employee = currentUserService.getCurrentEmployee();

        PurchaseOrder po = PurchaseOrder.builder()
                .supplier(supplier)
                .orderDate(LocalDate.now())
                .expectedDeliveryDate(request.getExpectedDeliveryDate())
                .status(PurchaseOrderStatus.DRAFT) // Mặc định là bản nháp
                .createdBy(employee)
                .build();

        PurchaseOrder savedPo = purchaseOrderRepo.save(po);

        // Logic thêm items đã được loại bỏ
        return purchaseOrderMapper.toDetailResponse(savedPo);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderDetailResponse findPurchaseOrderById(Integer id) {
        PurchaseOrder po = purchaseOrderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn mua hàng với ID: " + id));
        return purchaseOrderMapper.toDetailResponse(po);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrderSummaryResponse> findAllPurchaseOrders(Pageable pageable, PurchaseOrderStatus status) {
        Page<PurchaseOrder> purchaseOrderPage;
        if (status != null) {
            // Nếu có tham số status, lọc theo trạng thái
            purchaseOrderPage = purchaseOrderRepo.findByStatus(status, pageable);
        } else {
            // Nếu không, lấy tất cả
            purchaseOrderPage = purchaseOrderRepo.findAll(pageable);
        }

        // Chuyển đổi Page<Entity> thành Page<DTO>
        return purchaseOrderPage.map(purchaseOrderMapper::toSummaryResponse);
    }
    
    @Override
    @Transactional
    public PurchaseOrderDetailResponse approvePurchaseOrder(Integer id) {
        PurchaseOrder po = findPurchaseOrderByIdInternal(id);

        // Chỉ có thể duyệt đơn hàng ở trạng thái DRAFT hoặc SUBMITTED
        if (po.getStatus() != PurchaseOrderStatus.DRAFT && po.getStatus() != PurchaseOrderStatus.SUBMITTED) {
            throw new BadRequestException("Không thể duyệt đơn hàng ở trạng thái " + po.getStatus());
        }

        po.setStatus(PurchaseOrderStatus.APPROVED);
        PurchaseOrder updatedPo = purchaseOrderRepo.save(po);
        return purchaseOrderMapper.toDetailResponse(updatedPo);
    }

    @Override
    @Transactional
    public PurchaseOrderDetailResponse cancelPurchaseOrder(Integer id) {
        PurchaseOrder po = findPurchaseOrderByIdInternal(id);

        // Không thể hủy đơn hàng đã hoàn thành
        if (po.getStatus() == PurchaseOrderStatus.COMPLETED) {
            throw new BadRequestException("Không thể hủy đơn hàng đã hoàn thành.");
        }

        po.setStatus(PurchaseOrderStatus.CANCELLED);
        PurchaseOrder updatedPo = purchaseOrderRepo.save(po);
        return purchaseOrderMapper.toDetailResponse(updatedPo);
    }

    // Phương thức private helper để tránh lặp code
    private PurchaseOrder findPurchaseOrderByIdInternal(Integer id) {
        return purchaseOrderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn mua hàng với ID: " + id));
    }
}