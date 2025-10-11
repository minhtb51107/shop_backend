
package com.example.demo.supplychain.repository;

import com.example.demo.supplychain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // JpaRepository<Employee, Integer> có nghĩa là:
    // - Employee: Repository này làm việc với Entity Employee.
    // - Integer: Kiểu dữ liệu của khóa chính (@Id) trong Employee là Integer.
}