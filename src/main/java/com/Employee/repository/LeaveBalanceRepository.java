package com.Employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Employee.entity.LeaveBalance;

public interface LeaveBalanceRepository
        extends JpaRepository<LeaveBalance, Long> {

    LeaveBalance findByEmployeeId(Long employeeId);

}