package com.example.demo.sale.service;

import com.example.demo.sale.dto.request.CreateReturnRequest;
import com.example.demo.sale.dto.response.ReturnResponse;

import java.util.List;

public interface ReturnService {
    ReturnResponse createReturn(CreateReturnRequest request);
    ReturnResponse getReturnById(Long id);
    List<ReturnResponse> findAllReturns();
}