package com.Employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.Attendance;
import com.Employee.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/second")
    public String hello() {
        return "hello Spring Boot";
    }

    @PostMapping("/clock-in/{employeeId}")
    public Attendance clockIn(@PathVariable Long employeeId) {
        return attendanceService.clockIn(employeeId);
    }

    @PostMapping("/clock-out/{employeeId}")
    public Attendance clockOut(@PathVariable Long employeeId) {
        return attendanceService.clockOut(employeeId);
    }

    @GetMapping("/{employeeId}")
    public List<Attendance> history(@PathVariable Long employeeId) {
        return attendanceService.getHistory(employeeId);
    }
}