package com.example.demo.product.dto.request;

import lombok.Getter;
import lombok.Setter;

// DTO này chỉ chứa những thông tin cần thiết để tạo hoặc cập nhật một Brand.
@Getter
@Setter
public class BrandRequest {
    // Không cần ID ở đây, vì ID cho việc tạo mới sẽ do DB tự sinh,
    // và ID cho việc cập nhật sẽ được truyền qua @PathVariable.
    private String name;
}
