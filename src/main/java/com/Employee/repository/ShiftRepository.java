package com.Employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Employee.entity.Shift;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByEmployeeId(Long employeeId);
}