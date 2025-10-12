package com.example.demo.supplychain.service.impl;

import com.example.demo.supplychain.dto.response.SupplierResponse;
import com.example.demo.supplychain.entity.Supplier;
import com.example.demo.supplychain.mapper.SupplierMapper;
import com.example.demo.supplychain.repository.SupplierRepository;
import com.example.demo.supplychain.service.SupplierService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() { // <-- Thay đổi kiểu trả về
        return supplierRepository.findAll().stream()
               .map(supplierMapper::toSupplierResponse)
               .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Integer id) {
        return supplierRepository.findById(id);
    }

    @Override
    @Transactional
    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public Supplier updateSupplier(Integer id, Supplier supplierDetails) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
        
        supplier.setName(supplierDetails.getName());
        supplier.setContactPerson(supplierDetails.getContactPerson());
        supplier.setEmail(supplierDetails.getEmail());
        
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Integer id) {
        if (!supplierRepository.existsById(id)) {
            throw new EntityNotFoundException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return supplierRepository.existsById(id);
    }
}
