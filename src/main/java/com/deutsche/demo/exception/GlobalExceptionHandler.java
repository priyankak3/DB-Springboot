package com.deutsche.demo.exception;

import com.deutsche.demo.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Employee> handleEmployeeNotFoundException(EmployeeNotFoundException e) {
        LOG.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("message", e.getMessage())
                .body(null);
    }
}
