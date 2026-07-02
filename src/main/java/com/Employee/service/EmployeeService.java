package com.Employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Employee.DTO.RegisterRequest;
import com.Employee.entity.Employee;
import com.Employee.repository.EmployeeRepo;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee register(RegisterRequest request) {

        Employee employee = new Employee();

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());

        employee.setPassword(
                passwordEncoder.encode(request.getPassword()));

        employee.setRole(
                Employee.Role.valueOf(request.getRole()));

        employee.setManagerId(request.getManagerId());

        return employeeRepository.save(employee);
    }

    public Employee saveEmployee(Employee employee) {

        employee.setPassword(
                passwordEncoder.encode(employee.getPassword()));

        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));
    }
}