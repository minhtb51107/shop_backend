package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.response.ShipmentResponse;
import com.example.demo.sale.entity.Shipment;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShipmentMapper {
    public ShipmentResponse toDto(Shipment shipment) {
        if (shipment == null) {
            return null;
        }
        ShipmentResponse dto = new ShipmentResponse();
        dto.setId(shipment.getId());
        dto.setOrderId(shipment.getOrder().getId());
        dto.setTrackingCode(shipment.getTrackingCode());
        dto.setStatus(shipment.getStatus());
        dto.setShippedAt(shipment.getShippedAt());
        return dto;
    }

    public List<ShipmentResponse> toDtoList(List<Shipment> shipments) {
        return shipments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}