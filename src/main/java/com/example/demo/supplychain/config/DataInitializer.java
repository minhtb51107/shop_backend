package com.example.demo.supplychain.config;

import com.example.demo.supplychain.entity.*;
import com.example.demo.supplychain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Khởi tạo dữ liệu test cho Module 2
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public void run(String... args) throws Exception {
        // Chỉ tạo dữ liệu nếu chưa có
        if (employeeRepository.count() == 0) {
            createTestData();
        }
    }

    private void createTestData() {
        // Tạo Employee test
        Employee employee = Employee.builder()
                .fullname("Nguyễn Văn A")
                .email("nguyenvana@company.com")
                .phone("0123456789")
                .position("Purchasing Manager")
                .department("Supply Chain")
                .isActive(true)
                .build();
        employeeRepository.save(employee);

        // Tạo Product Variants test
        ProductVariant variant1 = ProductVariant.builder()
                .sku("LAPTOP-001")
                .name("Laptop Dell Inspiron 15")
                .description("Laptop Dell Inspiron 15 inch, Intel Core i5")
                .unitPrice(new BigDecimal("15000000"))
                .currentStock(10)
                .minStockLevel(5)
                .isActive(true)
                .build();
        productVariantRepository.save(variant1);

        ProductVariant variant2 = ProductVariant.builder()
                .sku("MOUSE-001")
                .name("Wireless Mouse Logitech")
                .description("Wireless Mouse Logitech M705")
                .unitPrice(new BigDecimal("500000"))
                .currentStock(50)
                .minStockLevel(20)
                .isActive(true)
                .build();
        productVariantRepository.save(variant2);

        // Tạo Supplier test (nếu chưa có)
        if (supplierRepository.count() == 0) {
            Supplier supplier = Supplier.builder()
                    .name("Tech Supplier Co.")
                    .contactPerson("Trần Thị B")
                    .email("contact@techsupplier.com")
                    .build();
            supplierRepository.save(supplier);
        }

        // Tạo Warehouse test (nếu chưa có)
        if (warehouseRepository.count() == 0) {
            Warehouse warehouse = Warehouse.builder()
                    .name("Main Warehouse")
                    .address("123 Main Street, Ho Chi Minh City")
                    .build();
            warehouseRepository.save(warehouse);
        }
    }
}
