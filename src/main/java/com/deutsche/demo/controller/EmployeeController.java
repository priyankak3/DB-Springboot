package com.deutsche.demo.controller;

import com.deutsche.demo.model.Employee;
import com.deutsche.demo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins="http://localhost:5173")
@RestController
@RequestMapping("/emp")
public class EmployeeController {

    //For logging
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService empService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        LOG.info("GET /emp");
        return empService.getAllEmployees();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
        LOG.info("GET /emp/{}", id);
        return ResponseEntity.ok(empService.getEmployeeById(id));
    }
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> findByName(@RequestParam String name) {
        LOG.info("GET /emp/search?name={}", name);
        return ResponseEntity.ok(empService.findByEmployeeName(name));
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee emp) {
        LOG.info("POST /emp");
        return ResponseEntity.ok(empService.addEmployee(emp));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable Integer id, @RequestBody Employee emp) {
        LOG.info("PUT /emp/{}", id);
        return ResponseEntity.ok(empService.updateEmployeeById(id, emp));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable Integer id) {
        LOG.info("DELETE /emp/{}", id);
        Employee deletedEmployee = empService.deleteEmployeeById(id);
        return ResponseEntity.ok(deletedEmployee);
    }

    @GetMapping("/searchBySalary")
    public ResponseEntity<List<Employee>> findBySalaryGreaterThan(@RequestParam(name = "salary") Double salary) {
        LOG.info("GET /emp/search?salary={}", salary);
        List<Employee> employees = empService.findBySalaryGreaterThan(salary);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/searchByName")
    public ResponseEntity<List<Employee>> findByNameIgnoreCase(@RequestParam(name = "name") String name) {
        LOG.info("GET /emp/searchByName?name={}", name);
        List<Employee> employees = empService.findByNameIgnoreCase(name);
        return ResponseEntity.ok(employees);
    }

}
