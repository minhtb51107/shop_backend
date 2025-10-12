package com.example.demo.supplychain.service;

import com.example.demo.supplychain.dto.response.SupplierResponse;
import com.example.demo.supplychain.entity.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService {
    
    List<SupplierResponse> getAllSuppliers();
    
    Optional<Supplier> getSupplierById(Integer id);
    
    Supplier createSupplier(Supplier supplier);
    
    Supplier updateSupplier(Integer id, Supplier supplierDetails);
    
    void deleteSupplier(Integer id);
    
    boolean existsById(Integer id);
}
