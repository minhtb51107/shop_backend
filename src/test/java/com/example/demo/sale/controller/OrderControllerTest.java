package com.example.demo.sale.controller;

import com.example.demo.sale.dto.request.CreateOrderRequest;
import com.example.demo.sale.dto.response.OrderResponse;
import com.example.demo.sale.service.OrderService;
import com.example.demo.shared.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@WebMvcTest(OrderController.class) // Chỉ định chỉ test OrderController
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService; // Giả lập OrderService

    @Autowired
    private ObjectMapper objectMapper;

    // Test Case: TC_ORDER_01
    @Test
    @WithMockUser // Cần đăng nhập để tạo đơn hàng
    public void whenCreateOrder_givenValidRequest_shouldReturnCreated() throws Exception {
        // 1. Given
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        // ... (Bạn cần set các trường dữ liệu hợp lệ cho orderRequest) ...
        // ví dụ: orderRequest.setItems(List.of(new OrderItemRequest(...)));
        
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(1L);

        given(orderService.createOrder(any(CreateOrderRequest.class))).willReturn(orderResponse);

        // 2. When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount", is(500.0)));
    }
    
    // Test Case: TC_ORDER_04
    @Test
    @WithMockUser // Giả lập user "user@example.com" đã đăng nhập
    public void whenGetOrderById_givenUserIsOwner_shouldReturnOrder() throws Exception {
        // 1. Given
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        orderResponse.setCustomerId(1L); // Giả sử

        // "Dạy" service: Khi gọi getOrderById với ID 1, trả về orderResponse
        given(orderService.getOrderById(eq(1L))).willReturn(orderResponse);

        // 2. When & Then
        mockMvc.perform(get("/api/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    // Test Case: TC_ORDER_05 (Mô phỏng 403, nhưng logic này thường nằm trong Service)
    @Test
    @WithMockUser
    public void whenGetOrderById_givenUserIsNotOwner_shouldReturnForbidden() throws Exception {
        // 1. Given
        
        // "Dạy" service: Khi user này gọi getOrderById, ném ra lỗi
        // (Logic kiểm tra "đúng chủ sở hữu" thường nằm trong Service)
        given(orderService.getOrderById(eq(2L)))
            .willThrow(new org.springframework.security.access.AccessDeniedException("Access Denied"));

        // 2. When & Then
        mockMvc.perform(get("/api/orders/{id}", 2L))
                .andExpect(status().isForbidden()); // Mong đợi 403
    }
}