package com.example.demo.sale.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnResponse {
    private Long id;
    private Long orderId;
    private String reason;
    private String status;
    private Long createdByEmployeeId;
    private List<ReturnItemResponse> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnItemResponse {
        private Long id;
        private Long orderItemId;
        private Integer quantity;
        private String reason;
    }
}