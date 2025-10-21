package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.BrandRequest;
import com.example.demo.product.dto.response.BrandResponse;
import com.example.demo.product.entity.Brand;
import com.example.demo.product.mapper.BrandMapper;
import com.example.demo.product.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    // Test Case: TC_BRAND_01
    @Test
    void createBrand_shouldReturnBrandResponse() {
        // 1. Given (Thiết lập)
        BrandRequest request = new BrandRequest();
        request.setName("Samsung");

        Brand brandToSave = new Brand(); // Entity mà mapper tạo ra
        brandToSave.setName("Samsung");

        Brand savedBrand = new Brand(); // Entity sau khi repo lưu
        savedBrand.setId(1);
        savedBrand.setName("Samsung");

        BrandResponse expectedResponse = new BrandResponse(1, "Samsung");

        // Dạy mock
        given(brandMapper.toEntity(request)).willReturn(brandToSave);
        given(brandRepository.save(brandToSave)).willReturn(savedBrand);
        given(brandMapper.toResponse(savedBrand)).willReturn(expectedResponse);

        // 2. When (Hành động)
        BrandResponse actualResponse = brandService.createBrand(request);

        // 3. Then (Kiểm chứng)
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        
        // ** Quan trọng: Xác minh rằng không có phương thức "findByName" nào được gọi **
        //verify(brandRepository, never()).findByName(anyString()); // Giả sử findByName trả về String
        verify(brandRepository, times(1)).save(brandToSave); // Chỉ gọi save 1 lần
    }

    // Test Case: TC_BRAND_02
    @Test
    void getBrandById_whenBrandExists_shouldReturnOptionalOfBrandResponse() {
        // 1. Given
        Integer brandId = 1;
        Brand brandEntity = new Brand(brandId, "Samsung");
        BrandResponse expectedResponse = new BrandResponse(brandId, "Samsung");

        given(brandRepository.findById(brandId)).willReturn(Optional.of(brandEntity));
        given(brandMapper.toResponse(brandEntity)).willReturn(expectedResponse);

        // 2. When
        Optional<BrandResponse> actualResponse = brandService.getBrandById(brandId);

        // 3. Then
        assertTrue(actualResponse.isPresent());
        assertEquals(expectedResponse.getName(), actualResponse.get().getName());
    }

    // Test Case: TC_BRAND_03
    @Test
    void getBrandById_whenBrandNotFound_shouldReturnOptionalEmpty() {
        // 1. Given
        Integer brandId = 999;
        given(brandRepository.findById(brandId)).willReturn(Optional.empty());

        // 2. When
        Optional<BrandResponse> actualResponse = brandService.getBrandById(brandId);

        // 3. Then
        assertTrue(actualResponse.isEmpty());
        // Đảm bảo mapper không được gọi
        verify(brandMapper, never()).toResponse(any(Brand.class));
    }
    
    // Test Case: TC_BRAND_04
    @Test
    void getAllBrands_whenTwoBrandsExist_shouldReturnListOfTwo() {
        // 1. Given
        Brand brand1 = new Brand(1, "Samsung");
        Brand brand2 = new Brand(2, "Apple");
        List<Brand> brandList = List.of(brand1, brand2);

        BrandResponse response1 = new BrandResponse(1, "Samsung");
        BrandResponse response2 = new BrandResponse(2, "Apple");

        given(brandRepository.findAll()).willReturn(brandList);
        given(brandMapper.toResponse(brand1)).willReturn(response1);
        given(brandMapper.toResponse(brand2)).willReturn(response2);

        // 2. When
        List<BrandResponse> actualList = brandService.getAllBrands();

        // 3. Then
        assertNotNull(actualList);
        assertEquals(2, actualList.size());
    }

    // Test Case: TC_BRAND_05
    @Test
    void getAllBrands_whenNoBrandsExist_shouldReturnEmptyList() {
        // 1. Given
        given(brandRepository.findAll()).willReturn(Collections.emptyList());

        // 2. When
        List<BrandResponse> actualList = brandService.getAllBrands();

        // 3. Then
        assertNotNull(actualList);
        assertTrue(actualList.isEmpty());
    }

    // Test Case: TC_BRAND_06
    @Test
    void deleteBrand_whenBrandExists_shouldReturnTrue() {
        // 1. Given
        Integer brandId = 1;
        given(brandRepository.existsById(brandId)).willReturn(true);
        // doNothing() là mặc định cho void method, nhưng chúng ta có thể gọi nó rõ ràng
        doNothing().when(brandRepository).deleteById(brandId);
        
        // 2. When
        boolean result = brandService.deleteBrand(brandId);

        // 3. Then
        assertTrue(result);
        // Xác minh rằng deleteById đã được gọi
        verify(brandRepository, times(1)).deleteById(brandId);
    }

    // Test Case: TC_BRAND_07
    @Test
    void deleteBrand_whenBrandNotFound_shouldReturnFalse() {
        // 1. Given
        Integer brandId = 999;
        given(brandRepository.existsById(brandId)).willReturn(false);
        
        // 2. When
        boolean result = brandService.deleteBrand(brandId);

        // 3. Then
        assertFalse(result);
        // Xác minh rằng deleteById KHÔNG BAO GIỜ được gọi
        verify(brandRepository, never()).deleteById(brandId);
    }
}