package com.example.demo.user.service.impl;

import com.example.demo.user.entity.Inventory;
import com.example.demo.user.entity.InventoryId;
import com.example.demo.user.repository.InventoryRepository;
import com.example.demo.user.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> findById(InventoryId id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public void deleteById(InventoryId id) {
        inventoryRepository.deleteById(id);
    }
}
