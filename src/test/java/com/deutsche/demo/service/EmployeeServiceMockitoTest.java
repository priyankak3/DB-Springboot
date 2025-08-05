package com.deutsche.demo.service;

import com.deutsche.demo.exception.EmployeeNotFoundException;
import com.deutsche.demo.model.Employee;
import com.deutsche.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceMockitoTest {

    @Mock
    private EmployeeRepository empRepository;

    @InjectMocks
    private EmployeeService empService;

    // ----------------------
    // Get all
    // ----------------------
    @Test
    void getAllEmployees_returnsList() {
        List<Employee> mockList = Arrays.asList(
                new Employee(1, "Alice", 50000.0),
                new Employee(2, "Bob", 60000.0)
        );
        given(empRepository.findAll()).willReturn(mockList);

        List<Employee> result = empService.getAllEmployees();

        assertEquals(2, result.size());
        then(empRepository).should().findAll();
        then(empRepository).shouldHaveNoMoreInteractions();
    }

    // ----------------------
    // Get by id
    // ----------------------
    @Test
    void getEmployeeById_found() {
        Employee e = new Employee(2, "Bob", 82000.0);
        given(empRepository.findById(2)).willReturn(Optional.of(e));

        Employee result = empService.getEmployeeById(2);

        assertNotNull(result);
        assertEquals("Bob", result.getName());
        then(empRepository).should().findById(2);
        then(empRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getEmployeeById_notFound_throws() {
        given(empRepository.findById(99)).willReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> empService.getEmployeeById(99));

        then(empRepository).should().findById(99);
        then(empRepository).shouldHaveNoMoreInteractions();
    }

    // ----------------------
    // Find by exact name
    // ----------------------
    @Test
    void findByEmployeeName_returnsList() {
        List<Employee> mock = Collections.singletonList(new Employee(4, "Tonu", 900000.0));
        given(empRepository.findByName("Tonu")).willReturn(mock);

        List<Employee> result = empService.findByEmployeeName("Tonu");

        assertEquals(1, result.size());
        assertEquals("Tonu", result.get(0).getName());
        then(empRepository).should().findByName("Tonu");
        then(empRepository).shouldHaveNoMoreInteractions();
    }

    // ----------------------
    // Add
    // ----------------------
    @Test
    void addEmployee_savesAndReturns() {
        Employee toSave = new Employee(null, "Marie", 45000.0);
        Employee saved  = new Employee(5, "Marie", 45000.0);

        given(empRepository.save(toSave)).willReturn(saved);

        Employee result = empService.addEmployee(toSave);

        assertEquals(5, result.getId());
        assertEquals("Marie", result.getName());
        then(empRepository).should().save(toSave);
        then(empRepository).shouldHaveNoMoreInteractions();
    }

    // ----------------------
    // Update by id
    // ----------------------
    @Test
    void updateEmployeeById_found_updatesFieldsAndSaves() {
        Employee existing = new Employee(10, "Old", 40000.0);
        Employee incoming = new Employee(null, "New", 55000.0);

        given(empRepository.findById(10)).willReturn(Optional.of(existing));
        // Return the same instance that is saved
        given(empRepository.save(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        Employee updated = empService.updateEmployeeById(10, incoming);

        assertEquals("New", updated.getName());
        assertEquals(55000.0, updated.getSalary());

        // Capture the entity passed to save() to verify fields
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        then(empRepository).should().findById(10);
        then(empRepository).should().save(captor.capture());
        Employee savedEntity = captor.getValue();
        assertEquals("New", savedEntity.getName());
        assertEquals(55000.0, savedEntity.getSalary());
        assertEquals(10, savedEntity.getId());

        then(empRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateEmployeeById_notFound_throws() {
        given(empRepository.findById(99)).willReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> empService.updateEmployeeById(99, new Employee()));

        then(empRepository).should().findById(99);
        then(empRepository).should(never()).save(any(Employee.class));
        then(empRepository).shouldHaveNoMoreInteractions();
    }

    // ----------------------
    // Delete by id
    // ----------------------
    @Test
    void deleteEmployeeById_found_deletesAndReturnsEmployee() {
        Employee existing = new Employee(7, "ToDelete", 39000.0);
        given(empRepository.findById(7)).willReturn(Optional.of(existing));
        willDoNothing().given(empRepository).deleteById(7);

        Employee deleted = empService.deleteEmployeeById(7);

        assertEquals(7, deleted.getId());
        assertEquals("ToDelete", deleted.getName());
        then(empRepository).should().findById(7);
        then(empRepository).should().deleteById(7);
        then(empRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteEmployeeById_notFound_throws() {
        given(empRepository.findById(77)).willReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> empService.deleteEmployeeById(77));

        then(empRepository).should().findById(77);
        then(empRepository).should(never()).deleteById(anyInt());
        then(empRepository).shouldHaveNoMoreInteractions();
    }

    // ----------------------
    // Salary greater than
    // ----------------------
    @Test
    void findBySalaryGreaterThan_returnsList() {
        List<Employee> mock = List.of(
                new Employee(1, "A", 100000.0),
                new Employee(2, "B", 120000.0)
        );
        // IMPORTANT: Stub EXACTLY the value you'll call with (or use anyDouble()).
        given(empRepository.findBySalaryGreaterThan(95000.0)).willReturn(mock);

        List<Employee> result = empService.findBySalaryGreaterThan(95000.0);

        assertEquals(2, result.size());
        then(empRepository).should().findBySalaryGreaterThan(95000.0);
        then(empRepository).shouldHaveNoMoreInteractions();
    }

    // ----------------------
    // Name ignore case
    // ----------------------
    @Test
    void findByNameIgnoreCase_returnsList() {
        List<Employee> mock = Arrays.asList(
                new Employee(2, "Bob", 50000.0),
                new Employee(3, "bob", 65000.0)
        );
        given(empRepository.findByNameIgnoreCase("bob")).willReturn(mock);

        List<Employee> result = empService.findByNameIgnoreCase("bob");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(e -> e.getName().equalsIgnoreCase("bob")));
        then(empRepository).should().findByNameIgnoreCase("bob");
        then(empRepository).shouldHaveNoMoreInteractions();
    }
}
