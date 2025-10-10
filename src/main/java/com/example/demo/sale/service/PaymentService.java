package com.example.demo.sale.service;

import com.example.demo.sale.dto.request.CreatePaymentRequest;
import com.example.demo.sale.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
}