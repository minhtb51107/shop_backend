package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.response.ShipmentResponse;
import com.example.demo.sale.entity.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    @Mapping(source = "order.id", target = "orderId")
    ShipmentResponse toDto(Shipment shipment);
}