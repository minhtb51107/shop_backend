package com.example.demo.sale.service.impl;

import com.example.demo.sale.dto.request.CreatePaymentRequest;
import com.example.demo.sale.dto.response.PaymentResponse;
import com.example.demo.sale.entity.Payment;
import com.example.demo.sale.repository.OrderRepository;
import com.example.demo.sale.repository.PaymentRepository;
import com.example.demo.sale.service.PaymentService;
import com.example.demo.sale.mapper.PaymentMapper;
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
        Payment payment = paymentMapper.toEntity(request);
        payment.setOrder(orderRepository.findById(request.getOrderId()).orElseThrow());
        payment.setStatus("COMPLETED");

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }
}