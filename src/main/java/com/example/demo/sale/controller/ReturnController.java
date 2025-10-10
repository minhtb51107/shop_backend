package com.example.demo.sale.controller;

import com.example.demo.sale.dto.request.CreateReturnRequest;
import com.example.demo.sale.dto.response.ReturnResponse;
import com.example.demo.sale.service.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
public class ReturnController {
    private final ReturnService returnService;

    @PostMapping
    public ResponseEntity<ReturnResponse> createReturn(@RequestBody CreateReturnRequest request) {
        ReturnResponse response = returnService.createReturn(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}