package com.example.demo.sale.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CreateReturnRequest {
    private Long orderId;
    private String reason;
    private List<ReturnItemRequest> items;

    @Data
    public static class ReturnItemRequest {
        private Long orderItemId;
        private Integer quantity;
        private String reason;
    }
}