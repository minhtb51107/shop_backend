package com.example.demo.user.service;

import com.example.demo.user.entity.Brand;
import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> findAll();
    Optional<Brand> findById(Integer id);
    Brand save(Brand brand);
    void deleteById(Integer id);
}
