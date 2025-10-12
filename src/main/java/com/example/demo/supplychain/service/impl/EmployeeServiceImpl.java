//package com.example.demo.supplychain.service.impl;
//
//import com.example.demo.supplychain.entity.Employee;
//import com.example.demo.supplychain.repository.EmployeeRepository;
//import com.example.demo.supplychain.service.EmployeeService;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class EmployeeServiceImpl implements EmployeeService {
//
//    private final EmployeeRepository employeeRepository;
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Employee> getAllEmployees() {
//        return employeeRepository.findAll();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<Employee> getEmployeeById(Integer id) {
//        return employeeRepository.findById(id);
//    }
//
//    @Override
//    @Transactional
//    public Employee createEmployee(Employee employee) {
//        return employeeRepository.save(employee);
//    }
//
//    @Override
//    @Transactional
//    public Employee updateEmployee(Integer id, Employee employeeDetails) {
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
//        
//        employee.setFullname(employeeDetails.getFullname());
//        employee.setEmail(employeeDetails.getEmail());
//        employee.setPhone(employeeDetails.getPhone());
//        employee.setPosition(employeeDetails.getPosition());
//        employee.setDepartment(employeeDetails.getDepartment());
//        employee.setIsActive(employeeDetails.getIsActive());
//        
//        return employeeRepository.save(employee);
//    }
//
//    @Override
//    @Transactional
//    public void deleteEmployee(Integer id) {
//        if (!employeeRepository.existsById(id)) {
//            throw new EntityNotFoundException("Employee not found with id: " + id);
//        }
//        employeeRepository.deleteById(id);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public boolean existsById(Integer id) {
//        return employeeRepository.existsById(id);
//    }
//}
