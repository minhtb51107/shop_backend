package com.example.demo.product.service.impl;

import com.example.demo.product.dto.request.ProductCategoryRequest;
import com.example.demo.product.dto.response.ProductCategoryResponse;
import com.example.demo.product.entity.ProductCategory;
import com.example.demo.product.mapper.ProductCategoryMapper;
import com.example.demo.product.repository.CategoryRepository;
import com.example.demo.product.service.ProductCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    // Ghi chú: Để nhất quán, bạn nên đổi tên CategoryRepository thành ProductCategoryRepository
    private final CategoryRepository categoryRepository;
    private final ProductCategoryMapper categoryMapper;

    public ProductCategoryServiceImpl(CategoryRepository categoryRepository, ProductCategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCategoryResponse> getCategoryById(Integer id) {
        return categoryRepository.findById(id).map(categoryMapper::toResponse);
    }

    @Override
    @Transactional
    public ProductCategoryResponse createCategory(ProductCategoryRequest request) {
        ProductCategory newCategory = categoryMapper.toEntity(request);
        ProductCategory savedCategory = categoryRepository.save(newCategory);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional
    public Optional<ProductCategoryResponse> updateCategory(Integer id, ProductCategoryRequest request) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    categoryMapper.updateEntityFromRequest(request, existingCategory);
                    ProductCategory updatedCategory = categoryRepository.save(existingCategory);
                    return categoryMapper.toResponse(updatedCategory);
                });
    }

    @Override
    @Transactional
    public boolean deleteCategory(Integer id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}