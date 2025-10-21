package com.example.demo.supplychain.service.impl;

import com.example.demo.supplychain.dto.response.SupplierResponse;
import com.example.demo.supplychain.entity.Supplier;
import com.example.demo.supplychain.mapper.SupplierMapper;
import com.example.demo.supplychain.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    // Test Case: TC_SUP_01
    @Test
    void createSupplier_shouldReturnSavedSupplierEntity() {
        // 1. Given
        // Service này nhận và trả về Entity, theo đúng code của bạn
        Supplier supplierToSave = new Supplier();
        supplierToSave.setName("NCC A");
        supplierToSave.setEmail("ncc.a@test.com");
        
        Supplier savedSupplier = new Supplier();
        savedSupplier.setId(1); // Service dùng Integer
        savedSupplier.setName("NCC A");
        savedSupplier.setEmail("ncc.a@test.com");

        given(supplierRepository.save(supplierToSave)).willReturn(savedSupplier);

        // 2. When
        Supplier actualSupplier = supplierService.createSupplier(supplierToSave);

        // 3. Then
        assertNotNull(actualSupplier);
        assertEquals(savedSupplier.getId(), actualSupplier.getId());
        
        // ** Quan trọng: Xác minh không gọi findByEmail **
        //verify(supplierRepository, never()).findByEmail(anyString());
        verify(supplierRepository, times(1)).save(supplierToSave);
    }

    // Test Case: TC_SUP_02
    @Test
    void getSupplierById_whenSupplierExists_shouldReturnOptionalOfSupplierEntity() {
        // 1. Given
        Integer supplierId = 1;
        Supplier supplierEntity = new Supplier();
        supplierEntity.setId(supplierId);
        supplierEntity.setName("NCC A");

        given(supplierRepository.findById(supplierId)).willReturn(Optional.of(supplierEntity));

        // 2. When
        Optional<Supplier> actualResult = supplierService.getSupplierById(supplierId);

        // 3. Then
        assertTrue(actualResult.isPresent());
        assertEquals(supplierId, actualResult.get().getId());
        // Service này trả về Entity, nên không gọi mapper
        verify(supplierMapper, never()).toSupplierResponse(any(Supplier.class));
    }

    // Test Case: TC_SUP_03
    @Test
    void getSupplierById_whenSupplierNotFound_shouldReturnOptionalEmpty() {
        // 1. Given
        Integer supplierId = 999;
        given(supplierRepository.findById(supplierId)).willReturn(Optional.empty());

        // 2. When
        Optional<Supplier> actualResult = supplierService.getSupplierById(supplierId);

        // 3. Then
        assertTrue(actualResult.isEmpty());
    }
    
    // Test Case: TC_SUP_04
    @Test
    void getAllSuppliers_whenOneSupplierExists_shouldReturnListOfOneResponse() {
        // 1. Given
        Supplier supplierEntity = new Supplier();
        supplierEntity.setId(1);
        supplierEntity.setName("NCC A");
        
        SupplierResponse supplierResponse = new SupplierResponse(1, "NCC A", "ncc.a@test.com", null);

        given(supplierRepository.findAll()).willReturn(List.of(supplierEntity));
        given(supplierMapper.toSupplierResponse(supplierEntity)).willReturn(supplierResponse);

        // 2. When
        List<SupplierResponse> actualList = supplierService.getAllSuppliers();

        // 3. Then
        assertNotNull(actualList);
        assertEquals(1, actualList.size());
        assertEquals("NCC A", actualList.get(0).getName());
    }

    // Test Case: TC_SUP_05
    @Test
    void deleteSupplier_whenSupplierExists_shouldDeleteSuccessfully() {
        // 1. Given
        Integer supplierId = 1;
        given(supplierRepository.existsById(supplierId)).willReturn(true);
        doNothing().when(supplierRepository).deleteById(supplierId);
        
        // 2. When
        // Dùng assertDoesNotThrow để xác nhận không có lỗi
        assertDoesNotThrow(() -> {
            supplierService.deleteSupplier(supplierId);
        });

        // 3. Then
        verify(supplierRepository, times(1)).existsById(supplierId);
        verify(supplierRepository, times(1)).deleteById(supplierId);
    }
    
    // Test Case: TC_SUP_06
    @Test
    void deleteSupplier_whenSupplierNotFound_shouldThrowEntityNotFoundException() {
        // 1. Given
        Integer supplierId = 999;
        given(supplierRepository.existsById(supplierId)).willReturn(false);

        // 2. When & 3. Then
        // Kiểm tra xem đúng exception đã được ném ra chưa
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            supplierService.deleteSupplier(supplierId);
        });
        
        assertEquals("Supplier not found with id: " + supplierId, exception.getMessage());
        
        // Đảm bảo deleteById không được gọi
        verify(supplierRepository, never()).deleteById(anyInt());
    }
}