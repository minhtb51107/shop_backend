package com.example.demo.sale.controller;

import com.example.demo.sale.dto.request.CreateReturnRequest;
import com.example.demo.sale.dto.response.ReturnResponse;
import com.example.demo.sale.service.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/returns")
@RequiredArgsConstructor
public class ReturnController {
    private final ReturnService returnService;

    @PostMapping
    public ResponseEntity<ReturnResponse> createReturn(@RequestBody CreateReturnRequest request) {
        ReturnResponse createdReturn = returnService.createReturn(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReturn);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReturnResponse> getReturnById(@PathVariable Long id) {
        ReturnResponse returnResponse = returnService.getReturnById(id);
        return ResponseEntity.ok(returnResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReturnResponse>> getAllReturns() {
        List<ReturnResponse> returns = returnService.findAllReturns();
        return ResponseEntity.ok(returns);
    }
}