package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreatePaymentRequest;
import com.example.demo.sale.dto.response.PaymentResponse;
import com.example.demo.sale.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "status", ignore = true)
    Payment toEntity(CreatePaymentRequest request);

    @Mapping(source = "order.id", target = "orderId")
    PaymentResponse toDto(Payment payment);
}