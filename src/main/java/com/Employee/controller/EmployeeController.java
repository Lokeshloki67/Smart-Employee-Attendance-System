package com.Employee.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.Employee;
import com.Employee.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable UUID id, Authentication authentication) {
        Employee caller = (Employee) authentication.getPrincipal();
        if (caller.getRole() != Employee.Role.MANAGER && !caller.getEmployeeId().equals(id)) {
            throw new AccessDeniedException("Not allowed");
        }
        return employeeService.getEmployeeById(id);
    }
}