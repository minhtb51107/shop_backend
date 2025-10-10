package com.example.demo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.user.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Tìm kiếm một Role bằng tên (duy nhất).
     * @param name Tên của Role (ví dụ: "ADMIN", "USER").
     * @return Optional chứa Role nếu tìm thấy.
     */
    Optional<Role> findByName(String name);
}