package com.example.demo.product.service;

import com.example.demo.product.dto.request.BrandRequest;
import com.example.demo.product.dto.response.BrandResponse;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<BrandResponse> getAllBrands();
    Optional<BrandResponse> getBrandById(Integer id);
    BrandResponse createBrand(BrandRequest request);
    Optional<BrandResponse> updateBrand(Integer id, BrandRequest request);
    boolean deleteBrand(Integer id);
}
