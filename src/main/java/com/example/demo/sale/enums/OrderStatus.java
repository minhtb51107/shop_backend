package com.example.demo.sale.enums;

public enum OrderStatus {
    PENDING,        // Đang chờ xác nhận
    CONFIRMED,      // Đã xác nhận (shop đã thấy đơn)
    PROCESSING,     // Đang xử lý / đóng gói
    SHIPPED,        // Đã gửi hàng
    DELIVERED,      // Đã giao thành công
    CANCELED,       // Đã hủy
    RETURNED        // Đã trả hàng
}