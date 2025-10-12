package com.example.demo.product.dto.response;

import lombok.Getter;
import lombok.Setter;

// DTO này định nghĩa dữ liệu Brand sẽ được trả về cho client.
@Getter
@Setter
public class BrandResponse {
    private Integer id;
    private String name;
}
