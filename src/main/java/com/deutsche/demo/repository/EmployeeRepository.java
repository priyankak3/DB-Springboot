package com.deutsche.demo.repository;

import com.deutsche.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Integer> {
    List<Employee> findByName(String name);
    List<Employee> findByNameIgnoreCase(String name);
    List<Employee> findBySalaryGreaterThan(Double salary);
}
