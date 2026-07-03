package com.Employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.Employee.DTO.AuthResponse;
import com.Employee.DTO.LoginRequest;
import com.Employee.Security.JwtUtil;
import com.Employee.entity.Employee;
import com.Employee.repository.EmployeeRepo;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/test")
    public String test() {
        return "Auth Controller Working";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        Employee employee = employeeRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                employee.getPassword())) {

            throw new RuntimeException("Invalid Password");
        }

        String token = jwtUtil.generateToken(employee.getEmail());

        return new AuthResponse(token);
    }
}