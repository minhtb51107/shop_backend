//package com.example.demo.user.service.impl;
//
//import com.example.demo.shared.exception.ResourceNotFoundException;
//import com.example.demo.user.dto.response.CustomerResponse;
//import com.example.demo.user.entity.Customer;
//import com.example.demo.user.mapper.CustomerMapper;
//import com.example.demo.user.repository.CustomerRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito
//class CustomerServiceImplTest {
//
//    @Mock // Tạo một đối tượng giả (mock) cho Repository
//    private CustomerRepository customerRepository;
//
//    @Mock // Tạo mock cho Mapper
//    private CustomerMapper customerMapper;
//
//    @InjectMocks // Tự động inject các mock ở trên vào service này
//    private CustomerServiceImpl customerService;
//
//    @Test
//    void getCustomerById_WhenCustomerExists_ShouldReturnCustomerResponse() {
//        // 1. GIVEN (Chuẩn bị dữ liệu)
//        Integer customerId = 1;
//        Customer customer = Customer.builder().id(customerId).fullname("Test User").build();
//        CustomerResponse expectedResponse = CustomerResponse.builder().id(customerId).fullname("Test User").build();
//
//        // Định nghĩa hành vi của mock: khi findById được gọi với customerId, trả về customer
//        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
//        when(customerMapper.toCustomerResponse(customer)).thenReturn(expectedResponse);
//
//        // 2. WHEN (Thực thi phương thức cần test)
//        CustomerResponse actualResponse = customerService.getCustomerById(customerId);
//
//        // 3. THEN (Kiểm tra kết quả)
//        assertNotNull(actualResponse);
//        assertEquals(expectedResponse.getId(), actualResponse.getId());
//        assertEquals(expectedResponse.getFullname(), actualResponse.getFullname());
//
//        // (Tùy chọn) Xác minh rằng phương thức của mock đã được gọi đúng 1 lần
//        verify(customerRepository, times(1)).findById(customerId);
//    }
//
//    @Test
//    void getCustomerById_WhenCustomerDoesNotExist_ShouldThrowResourceNotFoundException() {
//        // 1. GIVEN
//        Integer customerId = 99;
//        // Định nghĩa hành vi: khi findById được gọi, trả về Optional rỗng
//        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
//
//        // 2. WHEN & 3. THEN
//        // Kiểm tra xem exception có được ném ra không
//        assertThrows(ResourceNotFoundException.class, () -> {
//            customerService.getCustomerById(customerId);
//        });
//
//        // Xác minh phương thức của mapper không bao giờ được gọi
//        verify(customerMapper, never()).toCustomerResponse(any());
//    }
//}