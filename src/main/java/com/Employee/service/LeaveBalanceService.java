package com.Employee.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Employee.entity.Employee;
import com.Employee.entity.LeaveBalance;
import com.Employee.repository.EmployeeRepo;
import com.Employee.repository.LeaveBalanceRepository;

@Service
public class LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository repository;

    @Autowired
    private EmployeeRepo employeeRepo;

    public LeaveBalance getBalance(UUID employeeId) {

        LeaveBalance balance = repository.findByEmployeeEmployeeId(employeeId);

        if (balance == null) {
            Employee employee = employeeRepo.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            balance = new LeaveBalance();
            balance.setEmployee(employee);
            balance.setTotalPaidLeaves(24);
            balance.setUsedPaidLeaves(0.0);
            balance.setRemainingPaidLeaves(24.0);
            balance.setUnpaidLeavesTaken(0.0);
            balance = repository.save(balance);
        }

        return balance;
    }

    public LeaveBalance adjustTotalLeaves(UUID employeeId, Integer newTotal) {

        LeaveBalance balance = getBalance(employeeId);

        double diff = newTotal - balance.getTotalPaidLeaves();
        balance.setTotalPaidLeaves(newTotal);
        balance.setRemainingPaidLeaves(balance.getRemainingPaidLeaves() + diff);

        return repository.save(balance);
    }
}