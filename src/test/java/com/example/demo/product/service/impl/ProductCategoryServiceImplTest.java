package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.ProductCategoryRequest;
import com.example.demo.product.dto.response.ProductCategoryResponse;
import com.example.demo.product.entity.ProductCategory;
import com.example.demo.product.mapper.ProductCategoryMapper;
import com.example.demo.product.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductCategoryMapper categoryMapper;

    @InjectMocks
    private ProductCategoryServiceImpl categoryService;

    // Test Case: TC_CAT_01
    @Test
    void createCategory_shouldReturnCategoryResponse() {
        // 1. Given
        // SỬA LỖI: Bỏ "Mô tả Laptop" vì ProductCategoryRequest chỉ có 'name'
        ProductCategoryRequest request = new ProductCategoryRequest("Laptop");

        // SỬA LỖI: Bỏ "Mô tả Laptop" vì ProductCategory entity chỉ có 'id' và 'name'
        ProductCategory categoryToSave = new ProductCategory(null, "Laptop");
        ProductCategory savedEntity = new ProductCategory(1, "Laptop");

        // Giả định ProductCategoryResponse cũng chỉ có id và name
        // (Nếu file Response của bạn khác, bạn cần sửa dòng này)
        ProductCategoryResponse expectedResponse = new ProductCategoryResponse(1, "Laptop");

        given(categoryMapper.toEntity(request)).willReturn(categoryToSave);
        given(categoryRepository.save(categoryToSave)).willReturn(savedEntity);
        given(categoryMapper.toResponse(savedEntity)).willReturn(expectedResponse);

        // 2. When
        ProductCategoryResponse actualResponse = categoryService.createCategory(request);

        // 3. Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getName(), actualResponse.getName());

        // SỬA LỖI: Xóa dòng verify findByName vì nó không tồn tại
        // verify(categoryRepository, never()).findByName(anyString());
        verify(categoryRepository, times(1)).save(categoryToSave);
    }

    // Test Case: TC_CAT_02
    @Test
    void getCategoryById_whenCategoryExists_shouldReturnOptionalOfResponse() {
        // 1. Given
        Integer categoryId = 1;
        // SỬA LỖI: Bỏ "Mô tả"
        ProductCategory categoryEntity = new ProductCategory(categoryId, "Laptop");
        // Giả định response
        ProductCategoryResponse expectedResponse = new ProductCategoryResponse(categoryId, "Laptop");

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(categoryEntity));
        given(categoryMapper.toResponse(categoryEntity)).willReturn(expectedResponse);

        // 2. When
        Optional<ProductCategoryResponse> actualResponse = categoryService.getCategoryById(categoryId);

        // 3. Then
        assertTrue(actualResponse.isPresent());
        assertEquals(expectedResponse.getId(), actualResponse.get().getId());
    }

    // Test Case: TC_CAT_03 (Không thay đổi)
    @Test
    void getCategoryById_whenCategoryNotFound_shouldReturnOptionalEmpty() {
        // 1. Given
        Integer categoryId = 999;
        given(categoryRepository.findById(categoryId)).willReturn(Optional.empty());

        // 2. When
        Optional<ProductCategoryResponse> actualResponse = categoryService.getCategoryById(categoryId);

        // 3. Then
        assertTrue(actualResponse.isEmpty());
        verify(categoryMapper, never()).toResponse(any(ProductCategory.class));
    }

    // Test Case: TC_CAT_04
    @Test
    void getAllCategories_whenTwoCategoriesExist_shouldReturnListOfTwo() {
        // 1. Given
        // SỬA LỖI: Bỏ các tham số null
        ProductCategory cat1 = new ProductCategory(1, "Laptop");
        ProductCategory cat2 = new ProductCategory(2, "Phone");
        // Giả định response
        ProductCategoryResponse res1 = new ProductCategoryResponse(1, "Laptop");
        ProductCategoryResponse res2 = new ProductCategoryResponse(2, "Phone");

        given(categoryRepository.findAll()).willReturn(List.of(cat1, cat2));
        given(categoryMapper.toResponse(cat1)).willReturn(res1);
        given(categoryMapper.toResponse(cat2)).willReturn(res2);

        // 2. When
        List<ProductCategoryResponse> actualList = categoryService.getAllCategories();

        // 3. Then
        assertEquals(2, actualList.size());
    }

    // Test Case: TC_CAT_05 (Không thay đổi)
    @Test
    void getAllCategories_whenNoCategoriesExist_shouldReturnEmptyList() {
        // 1. Given
        given(categoryRepository.findAll()).willReturn(Collections.emptyList());

        // 2. When
        List<ProductCategoryResponse> actualList = categoryService.getAllCategories();

        // 3. Then
        assertNotNull(actualList);
        assertTrue(actualList.isEmpty());
    }

    // Test Case: TC_CAT_06 (Không thay đổi)
    @Test
    void deleteCategory_whenCategoryExists_shouldReturnTrue() {
        // 1. Given
        Integer categoryId = 1;
        given(categoryRepository.existsById(categoryId)).willReturn(true);

        // 2. When
        boolean result = categoryService.deleteCategory(categoryId);

        // 3. Then
        assertTrue(result);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    // Test Case: TC_CAT_07 (Không thay đổi)
    @Test
    void deleteCategory_whenCategoryNotFound_shouldReturnFalse() {
        // 1. Given
        Integer categoryId = 999;
        given(categoryRepository.existsById(categoryId)).willReturn(false);

        // 2. When
        boolean result = categoryService.deleteCategory(categoryId);

        // 3. Then
        assertFalse(result);
        verify(categoryRepository, never()).deleteById(anyInt());
    }
}