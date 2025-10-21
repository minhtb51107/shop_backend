//package com.example.demo.sale.controller;
//
//import com.example.demo.config.SecurityConfig;
//import com.example.demo.config.security.UserDetailsServiceImpl;
//import com.example.demo.shared.util.JwtUtil;
//import com.example.demo.sale.dto.request.CreateOrderRequest;
//import com.example.demo.sale.dto.response.OrderResponse;
//import com.example.demo.sale.service.OrderService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.bind.annotation.PathVariable; // QUAN TRỌNG: Import @PathVariable
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.hamcrest.Matchers.is;
//
///**
// * Test cho OrderController.
// * Vì các endpoint này cần xác thực, chúng ta sẽ import SecurityConfig
// * và sử dụng @WithMockUser để giả lập người dùng đã đăng nhập.
// */
//@WebMvcTest(controllers = OrderController.class) // Chỉ định đúng Controller cần test
//@Import(SecurityConfig.class) // Quan trọng: Import cấu hình security thật của bạn
//public class OrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private OrderService orderService;
//
//    // Cần Mock các bean là dependency của SecurityConfig và JwtAuthenticationFilter
//    @MockBean
//    private JwtUtil jwtUtil;
//
//    @MockBean
//    private UserDetailsServiceImpl userDetailsService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser(roles = "CUSTOMER") // Giả lập user đã đăng nhập với vai trò CUSTOMER
//    public void whenCreateOrder_givenValidRequest_shouldReturnCreated() throws Exception {
//        // 1. Given
//        CreateOrderRequest orderRequest = new CreateOrderRequest();
//        orderRequest.setCustomerId(1L);
//        orderRequest.setWarehouseId(1);
//        orderRequest.setShippingAddress("123 Test St");
//        orderRequest.setItems(new ArrayList<>()); // Cần một list rỗng, không phải null
//
//        OrderResponse orderResponse = new OrderResponse();
//        orderResponse.setId(1L);
//        orderResponse.setGrandTotal(new BigDecimal("500.00"));
//
//        given(orderService.createOrder(any(CreateOrderRequest.class))).willReturn(orderResponse);
//
//        // 2. When & Then
//        mockMvc.perform(post("/api/v1/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequest))
//                        .with(csrf()))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.grandTotal", is(500.0)));
//    }
//
//    /*
//     * LƯU Ý QUAN TRỌNG:
//     * Để các test case `getOrderById` chạy được, bạn cần sửa lại phương thức
//     * getOrderById trong file `OrderController.java` của bạn.
//     * Lỗi "Name for argument of type [java.lang.Long] not specified" xảy ra
//     * vì Spring không biết ánh xạ path variable `{id}` vào tham số nào.
//     *
//     * SỬA TỪ: public ResponseEntity<OrderResponse> getOrderById(Long id)
//     * THÀNH: public ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") Long id)
//     */
//
//    @Test
//    @WithMockUser(username = "customer@example.com") // Giả lập user cụ thể đã đăng nhập
//    public void whenGetOrderById_givenUserIsOwner_shouldReturnOrder() throws Exception {
//        // 1. Given
//        OrderResponse orderResponse = new OrderResponse();
//        orderResponse.setId(1L);
//        given(orderService.getOrderById(eq(1L))).willReturn(orderResponse);
//
//        // 2. When & Then
//        mockMvc.perform(get("/api/v1/orders/{id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)));
//    }
//
//    @Test
//    @WithMockUser(username = "another_user@example.com")
//    public void whenGetOrderById_givenUserIsNotOwner_shouldReturnForbidden() throws Exception {
//        // 1. Given
//        given(orderService.getOrderById(eq(2L)))
//            .willThrow(new AccessDeniedException("You do not have permission to view this order."));
//
//        // 2. When & Then
//        mockMvc.perform(get("/api/v1/orders/{id}", 2L))
//                .andExpect(status().isForbidden()); // Mong đợi 403 Forbidden
//    }
//}
//
