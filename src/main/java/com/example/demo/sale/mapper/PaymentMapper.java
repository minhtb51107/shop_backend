package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreatePaymentRequest;
import com.example.demo.sale.dto.response.PaymentResponse;
import com.example.demo.sale.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {
    public Payment toEntity(CreatePaymentRequest request) {
        if (request == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        return payment;
    }

    public PaymentResponse toDto(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentResponse dto = new PaymentResponse();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus());
        dto.setTransactionCode(payment.getTransactionCode());
        return dto;
    }

    public List<PaymentResponse> toDtoList(List<Payment> payments) {
        return payments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}