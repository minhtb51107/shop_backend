package com.example.demo.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO này chỉ chứa thông tin cần thiết để tạo hoặc cập nhật một Category.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryRequest {
    private String name;
}