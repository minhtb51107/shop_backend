package com.example.demo.sale.service.impl;

import com.example.demo.sale.dto.response.ShipmentResponse;
import com.example.demo.sale.entity.Shipment;
import com.example.demo.sale.mapper.ShipmentMapper;
import com.example.demo.sale.repository.ShipmentRepository;
import com.example.demo.sale.service.ShipmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;

    @Override
    public ShipmentResponse getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found with id: " + id));
        return shipmentMapper.toDto(shipment);
    }
}