package com.Employee.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.Employee;
import com.Employee.service.AnalyticsService;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/employee/{employeeId}")
    public Map<String, Object> employeeDashboard(@PathVariable UUID employeeId, Authentication authentication) {
        Employee caller = (Employee) authentication.getPrincipal();
        if (caller.getRole() != Employee.Role.MANAGER && !caller.getEmployeeId().equals(employeeId)) {
            throw new AccessDeniedException("Not allowed");
        }
        return analyticsService.getEmployeeDashboard(employeeId);
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public Map<String, Object> managerDashboard() {
        return analyticsService.getManagerDashboard();
    }
}