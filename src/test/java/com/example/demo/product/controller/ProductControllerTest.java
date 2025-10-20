//package com.example.demo.product.controller;
//
//import com.example.demo.product.dto.request.ProductRequest;
//import com.example.demo.product.dto.response.ProductResponse;
//import com.example.demo.product.service.ProductService;
//import com.example.demo.shared.exception.ResourceNotFoundException; // Import exception của bạn
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser; // Cần để test API cần quyền
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.hamcrest.Matchers.is;
//
//
//@WebMvcTest(ProductController.class) // Chỉ định chỉ test ProductController
//public class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductService productService; // Giả lập ProductService
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // Test Case: TC_PROD_01
//    @Test
//    @WithMockUser // Giả lập user đã đăng nhập (vì API này có thể đã được bảo vệ)
//    public void whenGetProductById_givenValidId_shouldReturnProduct() throws Exception {
//        // 1. Given
//        ProductResponse mockProduct = new ProductResponse();
//        mockProduct.setId((int) 1L);
//        mockProduct.setName("Test Product");
//        //mockProduct.setPrice(100.0);
//        
//        given(productService.getProductById((int)1L)).willReturn(mockProduct);
//
//        // 2. When & Then
//        mockMvc.perform(get("/api/products/{id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is("Test Product")))
//                .andExpect(jsonPath("$.price", is(100.0)));
//    }
//
//    // Test Case: TC_PROD_02
//    @Test
//    @WithMockUser
//    public void whenGetProductById_givenInvalidId_shouldReturnNotFound() throws Exception {
//        // 1. Given
//        given(productService.getProductById(9999L))
//            .willThrow(new ResourceNotFoundException("Product not found"));
//
//        // 2. When & Then
//        mockMvc.perform(get("/api/products/{id}", 9999L))
//                .andExpect(status().isNotFound()); // Mong đợi Status 404 Not Found
//    }
//
//    // Test Case: TC_PROD_03
//    @Test
//    @WithMockUser(roles = "ADMIN") // Giả lập user đăng nhập với quyền ADMIN
//    public void whenCreateProduct_givenValidRequest_shouldReturnCreated() throws Exception {
//        // 1. Given
//        ProductRequest productRequest = new ProductRequest(); // Tạo request hợp lệ
//        productRequest.setName("New Laptop");
//        productRequest.setPrice(1500.0);
//        // ... set các trường khác
//
//        ProductResponse createdProduct = new ProductResponse();
//        createdProduct.setId(10L);
//        createdProduct.setName("New Laptop");
//
//        given(productService.createProduct(any(ProductRequest.class))).willReturn(createdProduct);
//
//        // 2. When & Then
//        mockMvc.perform(post("/api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(productRequest)))
//                .andExpect(status().isCreated()) // Mong đợi Status 201 Created
//                .andExpect(jsonPath("$.name", is("New Laptop")));
//    }
//    
//    // Test Case: TC_PROD_06
//    @Test
//    @WithMockUser(roles = "CUSTOMER") // Giả lập user đăng nhập với quyền CUSTOMER
//    public void whenCreateProduct_givenUserIsCustomer_shouldReturnForbidden() throws Exception {
//        // 1. Given
//        ProductRequest productRequest = new ProductRequest();
//        productRequest.setName("New Laptop");
//
//        // 2. When & Then
//        // Vì Spring Security sẽ chặn trước khi gọi Controller,
//        // chúng ta không cần mock Service
//        mockMvc.perform(post("/api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(productRequest)))
//                .andExpect(status().isForbidden()); // Mong đợi Status 403 Forbidden
//    }
//}