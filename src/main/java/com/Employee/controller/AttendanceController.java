package com.Employee.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.Attendance;
import com.Employee.entity.Employee;
import com.Employee.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/clock-in/{employeeId}")
    public Attendance clockIn(@PathVariable UUID employeeId, Authentication authentication) {
        checkSelf(employeeId, authentication);
        return attendanceService.clockIn(employeeId);
    }

    @PostMapping("/clock-out/{employeeId}")
    public Attendance clockOut(@PathVariable UUID employeeId, Authentication authentication) {
        checkSelf(employeeId, authentication);
        return attendanceService.clockOut(employeeId);
    }

    @GetMapping("/{employeeId}")
    public List<Attendance> history(@PathVariable UUID employeeId, Authentication authentication) {
        checkSelf(employeeId, authentication);
        return attendanceService.getHistory(employeeId);
    }

    private void checkSelf(UUID employeeId, Authentication authentication) {
        Employee caller = (Employee) authentication.getPrincipal();
        if (caller.getRole() != Employee.Role.MANAGER && !caller.getEmployeeId().equals(employeeId)) {
            throw new AccessDeniedException("Not allowed");
        }
    }
}