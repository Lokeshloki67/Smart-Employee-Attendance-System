package com.Employee.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.LeaveBalance;
import com.Employee.service.LeaveBalanceService;

@RestController
@RequestMapping("/balance")
public class LeaveBalanceController {

    @Autowired
    private LeaveBalanceService service;

    @GetMapping("/{employeeId}")
    public LeaveBalance getBalance(@PathVariable UUID employeeId) {
        return service.getBalance(employeeId);
    }

    @PutMapping("/adjust/{employeeId}")
    @PreAuthorize("hasRole('MANAGER')")
    public LeaveBalance adjustTotal(@PathVariable UUID employeeId, @RequestParam Integer totalPaidLeaves) {
        return service.adjustTotalLeaves(employeeId, totalPaidLeaves);
    }
}