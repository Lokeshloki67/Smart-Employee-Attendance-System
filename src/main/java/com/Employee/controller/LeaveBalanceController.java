package com.Employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.Employee.entity.LeaveBalance;
import com.Employee.service.LeaveBalanceService;

@RestController
@RequestMapping("/balance")
public class LeaveBalanceController {

    @Autowired
    private LeaveBalanceService service;

    @PostMapping("/add")
    public LeaveBalance addBalance(
            @RequestBody LeaveBalance balance) {

        return service.addBalance(balance);
    }

    @GetMapping("/{employeeId}")
    public LeaveBalance getBalance(
            @PathVariable Long employeeId) {

        return service.getBalance(employeeId);
    }
}