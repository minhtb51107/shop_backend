package com.example.demo.supplychain.service.impl;

import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import com.example.demo.supplychain.entity.GoodsReceipt;
import com.example.demo.supplychain.entity.PurchaseOrder;
import com.example.demo.supplychain.entity.Warehouse;
import com.example.demo.supplychain.enums.PurchaseOrderStatus;
import com.example.demo.supplychain.repository.GoodsReceiptRepository;
import com.example.demo.supplychain.repository.PurchaseOrderRepository;
import com.example.demo.supplychain.repository.WarehouseRepository;
import com.example.demo.supplychain.security.CurrentUserService;
import com.example.demo.supplychain.service.GoodsReceiptService;
import com.example.demo.user.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service // Đánh dấu đây là một Spring Bean
@RequiredArgsConstructor
public class GoodsReceiptServiceImpl implements GoodsReceiptService {

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final WarehouseRepository warehouseRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceipt> getAllGoodsReceipts() {
        return goodsReceiptRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceipt getGoodsReceiptById(Integer id) {
        return goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu nhập kho với ID: " + id));
    }

    @Override
    @Transactional
    public GoodsReceipt createGoodsReceipt(Integer purchaseOrderId, Integer warehouseId, Integer employeeId) {
        // Lấy nhân viên thực hiện hành động từ context bảo mật
        Employee employee = currentUserService.getCurrentEmployee();

        // Tìm kho hàng
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kho với ID: " + warehouseId));

        PurchaseOrder purchaseOrder = null;
        // Chỉ thực hiện kiểm tra nếu có purchaseOrderId được cung cấp
        if (purchaseOrderId != null) {
            purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn mua hàng với ID: " + purchaseOrderId));

            // === LOGIC NGHIỆP VỤ ĐƯỢC THÊM VÀO ===
            // Kiểm tra trạng thái của đơn mua hàng.
            if (purchaseOrder.getStatus() != PurchaseOrderStatus.APPROVED) {
                throw new BadRequestException("Chỉ có thể tạo phiếu nhập cho đơn hàng đã ở trạng thái 'ĐÃ DUYỆT'.");
            }
            // =====================================
        }

        GoodsReceipt goodsReceipt = GoodsReceipt.builder()
                .purchaseOrder(purchaseOrder)
                .warehouse(warehouse)
                .receiptDate(LocalDate.now())
                .createdBy(employee)
                .build();

        return goodsReceiptRepository.save(goodsReceipt);
    }

    @Override
    @Transactional
    public void completeGoodsReceipt(Integer goodsReceiptId) {
    	// 1. Tìm phiếu nhập kho
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findById(goodsReceiptId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu nhập kho với ID: " + goodsReceiptId));

        // 2. Lấy đơn mua hàng liên quan (nếu có)
        PurchaseOrder purchaseOrder = goodsReceipt.getPurchaseOrder();

        if (purchaseOrder != null) {
            // 3. Kiểm tra logic nghiệp vụ: chỉ cập nhật đơn hàng nếu nó đang ở trạng thái APPROVED
            if (purchaseOrder.getStatus() == PurchaseOrderStatus.APPROVED) {
                // 4. Cập nhật trạng thái đơn hàng thành COMPLETED
                purchaseOrder.setStatus(PurchaseOrderStatus.COMPLETED);
                purchaseOrderRepository.save(purchaseOrder);
            }
            // Nếu đơn hàng ở trạng thái khác (ví dụ: CANCELLED), chúng ta không làm gì cả.
        }
    }

    @Override
    @Transactional
    public void deleteGoodsReceipt(Integer id) {
        if (!goodsReceiptRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy phiếu nhập kho với ID: " + id);
        }
        goodsReceiptRepository.deleteById(id);
    }
}