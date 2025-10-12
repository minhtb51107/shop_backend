package com.example.demo.sale.dto.response;

import lombok.Data;

@Data
public class ReturnItemResponse {
    private Long id;
    private Long returnId;
    private Long orderItemId;
    private Integer quantity;
    private String reason;
}