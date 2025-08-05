package com.deutsche.demo.service;

import com.deutsche.demo.controller.EmployeeController;
import com.deutsche.demo.exception.EmployeeNotFoundException;
import com.deutsche.demo.model.Employee;
import com.deutsche.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private EmployeeService empService;
    @Mock
    private EmployeeRepository empRepository;

    @InjectMocks
    private EmployeeService empService;

    @Test
    void getAllEmployees() {
        List<Employee> mockList = Arrays.asList(
                new Employee(1, "Alice", 50000.0),
                new Employee(2, "Bob", 60000.0)
        );
        given(empRepository.findAll()).willReturn(mockList);
        List<Employee> result = empService.getAllEmployees();

        assertEquals(2, result.size());
        then(empRepository).should().findAll();
        
    }

    @Test
    void getEmployeeById_found() {
        Employee e = new Employee(2, "Bob", 82000.0);
        given(empRepository.findById(2)).willReturn(Optional.of(e));

        Employee result = empService.getEmployeeById(2);

        assertNotNull(result);
        assertEquals("Bob", result.getName());
        then(empRepository).should().findById(2);
    }

    @Test
    void getEmployeeById_notFound_throws() {
        given(empRepository.findById(99)).willReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> empService.getEmployeeById(99));
        then(empRepository).should().findById(99);
    }

    @Test
    void findByEmployeeName_returnsList() {
        List<Employee> mock = Collections.singletonList(new Employee(4, "Tonu", 900000.0));
        given(empRepository.findByName("Tonu")).willReturn(mock);

        List<Employee> result = empService.findByEmployeeName("Tonu");

        assertEquals(1, result.size());
        assertEquals("Tonu", result.get(0).getName());
        then(empRepository).should().findByName("Tonu");
    }

    @Test
    void addEmployee_saves() {
        Employee toSave = new Employee(null, "Marie", 45000.0);
        Employee saved = new Employee(5, "Marie", 45000.0);
        given(empRepository.save(toSave)).willReturn(saved);

        Employee result = empService.addEmployee(toSave);

        assertEquals(5, result.getId());
        then(empRepository).should().save(toSave);
    }


    @Test
    void findBySalaryGreaterThan_returnsList() {
        List<Employee> mock = List.of(
                new Employee(1, "A", 90000.0),
                new Employee(2, "B", 95000.0)
        );
        given(empRepository.findBySalaryGreaterThan(95000.0)).willReturn(mock);

        List<Employee> result = empService.findBySalaryGreaterThan(95000.0);
        assertEquals(2, result.size());
        then(empRepository).should().findBySalaryGreaterThan(95000.0);

//        mockMvc.perform(get("/emp/searchBySalary").param("salary", "95000.0"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    void findByNameIgnoreCase_returnsList() {
        List<Employee> mock = Arrays.asList(
                new Employee(2, "Bob", 50000.0),
                new Employee(3, "Charlie", 65000.0)
        );
        given(empRepository.findByNameIgnoreCase("bob")).willReturn(mock);

        List<Employee> result = empService.findByNameIgnoreCase("bob");
        System.out.println(result);
        assertEquals("Bob", result.get(0).getName());
        then(empRepository).should().findByNameIgnoreCase("bob");
    }
}











//@Test
//    void updateEmployeeById_found_updates() {
//        Employee existing = new Employee(10, "Old", 40000.0);
//        Employee incoming = new Employee(null, "New", 55000.0);
//
//        given(empRepository.findById(10)).willReturn(Optional.of(existing));
//        given(empRepository.save(any(Employee.class)))
//                .willAnswer(invocation -> invocation.getArgument(0));
//
//        Employee updated = empService.updateEmployeeById(10, incoming);
//
//        assertEquals("New", updated.getName());
//        assertEquals(55000.0, updated.getSalary());
//        then(empRepository).should().findById(10);
//        then(empRepository).should().save(existing);
//    }
//
//    @Test
//    void updateEmployeeById_notFound_throws() {
//        given(empRepository.findById(99)).willReturn(Optional.empty());
//
//        assertThrows(EmployeeNotFoundException.class,
//                () -> empService.updateEmployeeById(99, new Employee()));
//        then(empRepository).should().findById(99);
//        then(empRepository).should(never()).save(any());
//    }
//
//@Test
//void deleteEmployeeById_found_deletes() {
//    Employee existing = new Employee(7, "ToDelete", 39000.0);
//    given(empRepository.findById(7)).willReturn(Optional.of(existing));
//    willDoNothing().given(empRepository).deleteById(7);
//
//    Employee deleted = empService.deleteEmployeeById(7);
//
//    assertEquals(7, deleted.getId());
//    then(empRepository).should().findById(7);
//    then(empRepository).should().deleteById(7);
//}
//
//@Test
//void deleteEmployeeById_notFound_throws() {
//    given(empRepository.findById(77)).willReturn(Optional.empty());
//
//    assertThrows(EmployeeNotFoundException.class,
//            () -> empService.deleteEmployeeById(77));
//    then(empRepository).should().findById(77);
//    then(empRepository).should(never()).deleteById(anyInt());
//}