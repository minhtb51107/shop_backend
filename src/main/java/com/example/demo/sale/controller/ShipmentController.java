package com.example.demo.sale.controller;

import com.example.demo.sale.dto.response.ShipmentResponse;
import com.example.demo.sale.service.ShipmentService; // Giả định có ShipmentService
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {
    private final ShipmentService shipmentService;

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getShipmentById(@PathVariable Long id) {
        ShipmentResponse response = shipmentService.getShipmentById(id);
        return ResponseEntity.ok(response);
    }
}