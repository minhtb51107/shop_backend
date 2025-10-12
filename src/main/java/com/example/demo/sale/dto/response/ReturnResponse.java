package com.example.demo.sale.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ReturnResponse {
    private Long id;
    private Long orderId;
    private String reason;
    private String status;
    private Integer createdByEmployeeId;
    private List<ReturnItemResponse> items;
}