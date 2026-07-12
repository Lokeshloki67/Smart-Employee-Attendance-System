package com.Employee.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.LeaveRequest;
import com.Employee.service.LeaveRequestService;

@RestController
@RequestMapping("/leave")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService service;

    @PostMapping("/apply/{employeeId}")
    public LeaveRequest applyLeave(@PathVariable UUID employeeId, @RequestBody LeaveRequest leaveRequest) {
        return service.applyLeave(employeeId, leaveRequest);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER')")
    public List<LeaveRequest> getAll() {
        return service.getAll();
    }

    @GetMapping("/{employeeId}")
    public List<LeaveRequest> getByEmployee(@PathVariable UUID employeeId) {
        return service.getByEmployeeId(employeeId);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public LeaveRequest approveLeave(@PathVariable UUID id) {
        return service.approve(id);
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public LeaveRequest rejectLeave(@PathVariable UUID id) {
        return service.reject(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public String deleteLeave(@PathVariable UUID id) {
        return service.delete(id);
    }
}