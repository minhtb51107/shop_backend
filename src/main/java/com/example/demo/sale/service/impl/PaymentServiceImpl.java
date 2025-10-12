package com.example.demo.sale.service.impl;

import com.example.demo.sale.dto.request.CreatePaymentRequest;
import com.example.demo.sale.dto.response.PaymentResponse;
import com.example.demo.sale.entity.Payment;
import com.example.demo.sale.repository.OrderRepository;
import com.example.demo.sale.repository.PaymentRepository;
import com.example.demo.sale.service.PaymentService;
import com.example.demo.sale.mapper.PaymentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        // Manual Validation
        if (request.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID must not be null.");
        }
        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Amount must not be null.");
        }
        if (request.getMethod() == null || request.getMethod().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method must not be empty.");
        }

        Payment payment = paymentMapper.toEntity(request);
        payment.setOrder(orderRepository.findById(request.getOrderId()).orElseThrow());
        payment.setStatus("COMPLETED");
        // Giả định logic tạo transaction code
        payment.setTransactionCode("TRANS-" + System.currentTimeMillis());

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toDto(payment);
    }
}