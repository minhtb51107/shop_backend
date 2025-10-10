package com.example.demo.sale.service;

import com.example.demo.sale.dto.request.CreateReturnRequest;
import com.example.demo.sale.dto.response.ReturnResponse;

public interface ReturnService {
    ReturnResponse createReturn(CreateReturnRequest request);
}