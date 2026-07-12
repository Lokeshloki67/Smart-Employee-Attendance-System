package com.Employee.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Employee.DTO.RegisterRequest;
import com.Employee.entity.Employee;
import com.Employee.entity.LeaveBalance;
import com.Employee.repository.EmployeeRepo;
import com.Employee.repository.LeaveBalanceRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee register(RegisterRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole(Employee.Role.valueOf(request.getRole()));
        employee.setDesignation(request.getDesignation());

        Employee saved = employeeRepository.save(employee);

        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(saved);
        balance.setTotalPaidLeaves(24);
        balance.setUsedPaidLeaves(0.0);
        balance.setRemainingPaidLeaves(24.0);
        balance.setUnpaidLeavesTaken(0.0);
        leaveBalanceRepository.save(balance);

        return saved;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}