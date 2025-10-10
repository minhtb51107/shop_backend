package com.example.demo.sale.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReturnRequest {
    private Long orderId;
    private String reason;
    private Long createdByEmployeeId;
    private List<ReturnItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnItemRequest {
        private Long orderItemId;
        private Integer quantity;
        private String reason;
    }
}