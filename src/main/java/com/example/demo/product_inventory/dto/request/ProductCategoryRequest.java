package com.example.demo.product_inventory.dto.request;

import lombok.Getter;
import lombok.Setter;

// DTO này chỉ chứa thông tin cần thiết để tạo hoặc cập nhật một Category.
@Getter
@Setter
public class ProductCategoryRequest {
    private String name;
}