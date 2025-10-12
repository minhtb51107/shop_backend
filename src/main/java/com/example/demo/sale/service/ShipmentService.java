package com.example.demo.sale.service;

import com.example.demo.sale.dto.response.ShipmentResponse;
import com.example.demo.sale.entity.Shipment;

public interface ShipmentService {
    ShipmentResponse getShipmentById(Long id);
    // Có thể thêm các phương thức khác như createShipment, updateShipmentStatus, v.v.
}