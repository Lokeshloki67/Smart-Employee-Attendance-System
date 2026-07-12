package com.Employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.Employee.DTO.AuthResponse;
import com.Employee.DTO.LoginRequest;
import com.Employee.DTO.RegisterRequest;
import com.Employee.Security.JwtUtil;
import com.Employee.entity.Employee;
import com.Employee.repository.EmployeeRepo;
import com.Employee.service.EmployeeService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        Employee employee = employeeService.register(request);
        String token = jwtUtil.generateToken(employee);
        return new AuthResponse(token, employee.getEmployeeId(), employee.getRole().name());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        Employee employee = employeeRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtUtil.generateToken(employee);
        return new AuthResponse(token, employee.getEmployeeId(), employee.getRole().name());
    }
}