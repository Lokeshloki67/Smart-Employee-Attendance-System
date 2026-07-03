package com.Employee.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Employee.entity.Attendance;
import com.Employee.entity.Employee;
import com.Employee.repository.AttendanceRepo;
import com.Employee.repository.EmployeeRepo;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    public Attendance clockIn(Long employeeId) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setClockInTime(LocalDateTime.now());

        return attendanceRepo.save(attendance);
    }

    public Attendance clockOut(Long employeeId) {

        Attendance attendance = attendanceRepo
                .findTopByEmployeeEmployeeIdOrderByClockInTimeDesc(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("No clock-in found"));

        LocalDateTime now = LocalDateTime.now();

        attendance.setClockOutTime(now);

        double hours = Duration
                .between(attendance.getClockInTime(), now)
                .toMinutes() / 60.0;

        attendance.setWorkHours(hours);

        return attendanceRepo.save(attendance);
    }

    public List<Attendance> getHistory(Long employeeId) {

        return attendanceRepo.findByEmployeeEmployeeId(employeeId);
    }
}