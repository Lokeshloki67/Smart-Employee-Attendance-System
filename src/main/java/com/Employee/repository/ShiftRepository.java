package com.Employee.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Employee.entity.Shift;

public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findByEmployeeEmployeeId(UUID employeeId);
}