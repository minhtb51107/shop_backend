package com.example.demo.sale.controller;

import com.example.demo.sale.dto.request.CreatePromotionRequest;
import com.example.demo.sale.dto.response.PromotionResponse;
import com.example.demo.sale.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody CreatePromotionRequest request) {
        PromotionResponse response = promotionService.createPromotion(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() {
        List<PromotionResponse> responses = promotionService.getAllPromotions();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updatePromotionStatus(@PathVariable Integer id, @RequestParam boolean isActive) {
        promotionService.updatePromotionStatus(id, isActive);
        return ResponseEntity.noContent().build();
    }
}