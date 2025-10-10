package com.example.demo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.user.entity.Permission;

import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    
    Optional<Permission> findByName(String name);
    
    // Thêm phương thức này
    Set<Permission> findByNameIn(Set<String> names);
}