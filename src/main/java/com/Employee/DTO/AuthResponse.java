package com.Employee.DTO;

import java.util.UUID;

public class AuthResponse {

    private String token;
    private UUID employeeId;
    private String role;

    public AuthResponse() {}

    public AuthResponse(String token, UUID employeeId, String role) {
        this.token = token;
        this.employeeId = employeeId;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}