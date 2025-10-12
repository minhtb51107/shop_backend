package com.example.demo.product.dto.response;

import lombok.Getter;
import lombok.Setter;

// DTO này định dạng dữ liệu trả về cho client.
@Getter
@Setter
public class ProductCategoryResponse {
    private Integer id;
    private String name;
}