package com.Employee.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Employee.entity.LeaveBalance;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, UUID> {
    LeaveBalance findByEmployeeEmployeeId(UUID employeeId);
}