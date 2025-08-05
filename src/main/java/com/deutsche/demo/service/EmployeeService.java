package com.deutsche.demo.service;

import com.deutsche.demo.exception.EmployeeNotFoundException;
import com.deutsche.demo.model.Employee;
import com.deutsche.demo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);
    @Autowired
    private EmployeeRepository empRepository;

    // Get all employees
    public List<Employee> getAllEmployees() {
        LOG.info("Fetching all employees");
        return empRepository.findAll();
    }
    public Employee getEmployeeById(Integer id) {
        LOG.info("Fetching employee with id={}", id);
//        Optional<Employee> employeeOptional = empRepository.findById(id);
//        if (employeeOptional.isPresent())
//            return employeeOptional.get();
//        else
//            throw new EmployeeNotFoundException(id);
        return empRepository.findById(id).orElseThrow(() -> {
                    LOG.warn("Employee with id={} not found", id);
                    return new EmployeeNotFoundException(id);
                });
    }
    public List<Employee> findByEmployeeName(String name) {
        LOG.info("Searching employees by name={}", name);
        List<Employee> employees = empRepository.findByName(name);
        if (employees.isEmpty()) {
            LOG.warn("No employees found with name={}", name);
        }
        return employees;
    }
    public Employee addEmployee(Employee employee) {
        LOG.info("Adding new employee: {}", employee);
        return empRepository.save(employee);
    }

    public Employee updateEmployeeById(Integer id, Employee updatedData) {
        LOG.info("Updating employee by id={}, with data={}", id, updatedData);

        return empRepository.findById(id).map(existing -> {
            existing.setName(updatedData.getName());
            existing.setSalary(updatedData.getSalary());
            return empRepository.save(existing);
        }).orElseThrow(() -> {
            LOG.warn("Employee with id={} not found for update", id);
            return new EmployeeNotFoundException(id);
        });
    }

    public Employee deleteEmployeeById(Integer id) {
        LOG.info("Deleting employee with id={}", id);
        return empRepository.findById(id).map(emp -> {
            empRepository.deleteById(id);
            LOG.info("Successfully Deleted employee: {}", emp);
            return emp;
        }).orElseThrow(() -> {
            LOG.warn("Cannot delete - employee with id={} not found", id);
            return new EmployeeNotFoundException(id);
        });
    }

    public List<Employee> findBySalaryGreaterThan(Double salary) {
        LOG.info("Finding employees with salary greater than: {}", salary);
        List<Employee> employees = empRepository.findBySalaryGreaterThan(salary);
        if (employees.isEmpty()) {
            LOG.warn("No employees found with salary greater than: {}", salary);
        }
        return employees;
    }

    public List<Employee> findByNameIgnoreCase(String  name) {
        LOG.info("Searching employees by name={}", name);
        List<Employee> employees = empRepository.findByNameIgnoreCase(name);
        if (employees.isEmpty()) {
            LOG.warn("No employees found with name={}", name);
        }else{
            LOG.warn("Found the employee with name={}", name);
        }
        return employees;
    }

}
