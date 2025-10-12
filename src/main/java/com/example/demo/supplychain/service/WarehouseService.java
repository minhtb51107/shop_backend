package com.example.demo.supplychain.service;

import com.example.demo.supplychain.entity.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {
    
    List<Warehouse> getAllWarehouses();
    
    Optional<Warehouse> getWarehouseById(Integer id);
    
    Warehouse createWarehouse(Warehouse warehouse);
    
    Warehouse updateWarehouse(Integer id, Warehouse warehouseDetails);
    
    void deleteWarehouse(Integer id);
    
    boolean existsById(Integer id);
}
