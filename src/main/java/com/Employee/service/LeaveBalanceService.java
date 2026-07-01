package com.Employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Employee.entity.LeaveBalance;
import com.Employee.repository.LeaveBalanceRepository;

@Service
public class LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository repository;

    public LeaveBalance addBalance(
            LeaveBalance balance) {

        return repository.save(balance);
    }

    public LeaveBalance getBalance(
            Long employeeId) {

        return repository.findByEmployeeId(employeeId);
    }
}