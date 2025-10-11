package com.example.demo.supplychain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.user.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}